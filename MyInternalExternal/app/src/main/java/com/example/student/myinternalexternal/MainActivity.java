package com.example.student.myinternalexternal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {


    private EditText editFileName;
    private EditText editText;
    private TextView textView;
    private boolean internal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        isChecked();
        permission();
        saveButtonOnclick();
        readButtonOnClick();
    }

    private void findViews() {
        editFileName = findViewById(R.id.edit_file_name);
        editText = findViewById(R.id.edit_text);
        textView = findViewById(R.id.text_view);
    }

    private void readButtonOnClick() {
        final Button readButton = findViewById(R.id.read_button);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internal) {
                    readFileInternal();
                } else {
                    readFileExternal();
                }
            }
        });
    }

    private void saveButtonOnclick() {
        final Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (internal) {
                    createFileInternal();
                } else {
                    writeFileExternal();
                }
            }
        });
    }


    private void isChecked() {
        final RadioButton internalButton = findViewById(R.id.internal_app);
        final RadioButton externalButton = findViewById(R.id.external_phone);
        internalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                internalButton.setChecked(true);
                externalButton.setChecked(false);
                internal = true;
            }
        });
        externalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                externalButton.setChecked(true);
                internalButton.setChecked(false);
                internal = false;
            }
        });
    }

    private void createFileInternal() {
        File file = new File(editFileName.getText().toString());
        if (file.exists()) file.mkdir();
        FileOutputStream fOut = null;
        try {
            fOut = openFileOutput(file.getPath(), MODE_PRIVATE);
            fOut.write(editText.getText().toString().getBytes());

        } catch (Exception e) {
            Log.i("fff", getString(R.string.fail));
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readFileInternal() {
        try {
            FileInputStream fis = openFileInput(editFileName.getText().toString());
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
                textView.setText(sb.toString());
            }
        } catch (IOException ignored) {
            Log.i("fff", getString(R.string.fail));
        }
    }

    private void writeFileExternal() {
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + File.separator + editFileName.getText().toString();
        String data = editText.getText().toString();
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter =null;
        try {
            File file = new File(path);
            if (!file.exists()) file.createNewFile();
            fOut = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

        } catch (Exception e) {
            Log.i("fff", getString(R.string.fail));
        }finally {
            try {
                fOut.close();
                myOutWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void readFileExternal() {
        File extStore = Environment.getExternalStorageDirectory();
        String path = extStore.getAbsolutePath() + File.separator + editFileName.getText().toString();
        String s;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader myReader =null;
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            while ((s = myReader.readLine()) != null) {
                stringBuilder.append(s);
            }

            this.textView.setText(stringBuilder.toString());
        } catch (IOException e) {
            Log.i("fff", getString(R.string.fail));
        }finally {
            try {
                myReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void permission() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                69);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 69:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permission();
                }
        }
    }
}




