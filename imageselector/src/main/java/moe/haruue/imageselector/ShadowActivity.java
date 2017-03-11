package moe.haruue.imageselector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ShadowActivity extends AppCompatActivity {

    public static final String TAG = "ImageSelector";
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final int REQUEST_CODE_PICK = 51321;
    public static final int REQUEST_CODE_CROP = 34887;
    public static final int REQUEST_CODE_PERMISSION = 41248;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        begin();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pick();
        } else {
            Toast.makeText(this, "授权遭拒", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK) {
                File tmp = new File(getExternalCacheDir(), "avatar_cache");
                if (tmp.exists()) {
                    tmp.delete();
                }
                crop(data.getData().toString(), Uri.fromFile(tmp).toString());
            } else if (requestCode == REQUEST_CODE_CROP) {
                if (DEBUG) {
                    Log.d(TAG, "Crop finished: " + data.getData().toString());
                }
                ImageSelector.listener.onImageSelectorSuccess(data.getData());
                finish();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (DEBUG) {
                Log.i(TAG, "Canceled");
            }
            ImageSelector.listener.onImageSelectorCancel();
            finish();
        }
        if (DEBUG) {
            Log.i(TAG, "end");
        }
    }

    private void begin() {
        if (DEBUG) {
            Log.i(TAG, "begin");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        } else {
            pick();
        }
    }

    private void pick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK);
    }

    public void crop(String imageUriString, String outputUriString) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.parse(imageUriString), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(outputUriString));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, REQUEST_CODE_CROP);
    }
}
