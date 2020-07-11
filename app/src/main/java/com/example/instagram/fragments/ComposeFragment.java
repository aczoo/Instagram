package com.example.instagram.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.instagram.R;
import com.example.instagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
//import org.junit.Assert;

public class ComposeFragment extends Fragment {
    public static final String TAG = "ComposeFragment";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public final static int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 1035;
    public String photoFileName = "photo.jpg";
    File photoFile;

    private final int width = 250;
    private final int height = 350;
    private EditText etCaption;
    private Button btnSubmit, btnCapture, btnGallery;
    private ProgressBar pb;
    private ImageView ivPicture, ivDelete;

    public ComposeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //find all of the components within the view
        etCaption = view.findViewById(R.id.etCaption);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnGallery = view.findViewById(R.id.btnGallery);
        pb = view.findViewById(R.id.pbLoading);
        ivPicture = view.findViewById(R.id.ivPicture);
        ivDelete = view.findViewById(R.id.ivDelete);

        //set listeners
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage();
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGallery();
            }
        });
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caption = etCaption.getText().toString();
                if (caption.isEmpty()) {
                    if (photoFile == null || ivPicture.getDrawable() == null) {
                        Toast.makeText(getContext(), "No Caption or Picture!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getContext(), "No Caption!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivPicture.getDrawable() == null) {
                    Toast.makeText(getContext(), "No Picture!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(caption, currentUser, photoFile);
            }
        });
    }

    private void deleteImage() {
        ivPicture.setImageResource(0);
        btnSubmit.setVisibility(View.GONE);
        ivPicture.setVisibility(View.GONE);
        ivDelete.setVisibility(View.GONE);
    }
    //opens camera and launches onActivityResult
    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if photo was taken with the camera
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ivPicture.setVisibility(View.VISIBLE);
                scale(BitmapFactory.decodeFile(photoFile.getAbsolutePath()), ivPicture);
                btnSubmit.setVisibility(View.VISIBLE);
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        //if photo was taken from the gallery
        else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            Uri photoUri = data.getData();
            //final File file = new File(Environment.getExternalStorageDirectory(), "read.me");
            //Uri uri = Uri.fromFile(file);
            //File auxFile = new File(uri.toString());
            //Assert.assertEqual(file.getAbsolutePath(), auxFile.getAbsolutePath());
            File file = new File(getPath(photoUri));
            photoFile = file;
            //issues getting a file from the uri :(
            ivPicture.setVisibility(View.VISIBLE);
            scale(loadFromUri(photoUri), ivPicture);
            btnSubmit.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
        }
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
        //gives ParseException java.lang.IllegalStateException: Unable to encode an unsaved ParseFile
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if (Build.VERSION.SDK_INT > 27) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void savePost(String caption, ParseUser currentUser, File photoFile) {
        //create new post to send to parse
        pb.setVisibility(ProgressBar.VISIBLE);
        Post post = new Post();
        post.setCaption(caption);
        post.setUser(currentUser);
        post.setLikes();
        post.setPicture(new ParseFile(photoFile));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving " + e);
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                } else {
                    pb.setVisibility(ProgressBar.GONE);
                    Log.i(TAG, "Post successful!");
                    etCaption.setText("");
                    deleteImage();
                }

            }
        });

    }

    public void scale(Bitmap b, ImageView iv) {
        if (b.getWidth() > b.getHeight()) {
            Log.i(TAG, "horizontal orientation");
            float factor = width / (float) b.getWidth();
            iv.getLayoutParams().height = (int) (b.getHeight() * factor);
            iv.getLayoutParams().width = width;
            iv.setImageBitmap(Bitmap.createScaledBitmap(b, width, iv.getLayoutParams().height, false));


        } else {
            Log.i(TAG, "vertical orientation");
            float factor = height / (float) b.getHeight();
            iv.setImageBitmap(Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, false));

        }

    }

}
