package com.example.lazeez;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lazeez.Model.User;
import com.example.lazeez.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //start firebase connectivity
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("please wait..");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                               Intent homeIntent = new Intent(SignIn.this,Home.class);
                               Common .currentUser= user;
                               startActivity(homeIntent);
                               finish();
                                // Toast.makeText(SignIn.this, "signin Successfull", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignIn.this, "wrong password or phone number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(SignIn.this,"User doesn't exist",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
