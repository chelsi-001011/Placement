package com.example.placement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Button signUpBtn;
    private EditText mEmail;
    private EditText mUsername;
    private EditText mpassword,mcpi;
    private String Email,cpi;
    private String Username;
    private RadioGroup radioGroup,radioGroupBranch;
    private RadioButton radioButton,radioButtonBranch;
    private String Password;
    private String type,branch;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        signUpBtn=(Button) findViewById(R.id.signUpbtn);
        mEmail=(EditText) findViewById(R.id.email);
        mUsername=(EditText) findViewById(R.id.name);
        mpassword=(EditText) findViewById(R.id.password);
        radioGroup=(RadioGroup) findViewById(R.id.radiogroup);
        mcpi=(EditText) findViewById(R.id.cpi);
        radioGroupBranch=(RadioGroup) findViewById(R.id.radiogroupbranch);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//                finish();

                Email=mEmail.getText().toString();
                Password=mpassword.getText().toString();
                int selectedId=radioGroup.getCheckedRadioButtonId();
                radioButton=(RadioButton) findViewById(selectedId);
                int selectedBranch=radioGroupBranch.getCheckedRadioButtonId();
                radioButtonBranch=(RadioButton) findViewById(selectedBranch);
                type=radioButton.getText().toString();
                Username=mUsername.getText().toString();
                cpi=mcpi.getText().toString();
                if(type.equals("Student")){
                    branch=radioButtonBranch.getText().toString();
                    setUpFirebase(Email,Password,Username,type,branch,cpi);


                }
                else{
                    setUpFirebase(Email,Password,Username,type);

                }
            }
        });
    }

    private void setUpFirebase(final String email,final   String password,final   String name,final String typeof,final String branch,final String cpi){
        if(email.equals("")||password.equals("")){
            Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sending Verification link", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null){
                                    final String user_id=user.getUid();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        //   Toast.makeText(RegisterActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                                        Map<String, Object> user = new HashMap<>();
                                                        user.put("email", email);
                                                        user.put("name", name);
                                                        user.put("type",typeof);
                                                        user.put("user_id",user_id);
                                                        user.put("branch",branch);
                                                        user.put("phone","");
                                                        user.put("resume","");
                                                        user.put("cpi",cpi);
                                                        db.collection("users")
                                                                .add(user)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });
//                                                        db.collection("applications").get()
//                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                        if(task.isSuccessful()){
//                                                                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                                ArrayList<String> br=(ArrayList<String>)document.get("branches");
//                                                                                String document_id=document.getString("document_id");
//                                                                                for(int i=0;i<br.size();i++){
//                                                                                    if(branch.equals(br.get(i))){
//                                                                                        DocumentReference note=db.collection("students_applications")
//                                                                                                .document(user_id+" "+document_id);
//                                                                                        note.update("name",name);
//                                                                                        note.update("user_id_student",user_id);
//                                                                                        note.update("document_id_company",document_id);
//                                                                                        note.update("selected",false);
//                                                                                        note.update("applied",false);
//                                                                                        note.update("rejected",false);
//                                                                                        note.update("resume","");
//                                                                                        break;
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                });
                                                    }
                                                    else{
                                                        Toast.makeText(RegisterActivity.this, "Could Not send verification Link", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }
    private void setUpFirebase(final String email, final   String password, final   String name, final String typeof){
        if(email.equals("")||password.equals("")){
            Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Sending Verification link", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null){
                                    final String user_id=user.getUid();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        //   Toast.makeText(RegisterActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                                        Map<String, Object> user = new HashMap<>();
                                                        user.put("email", email);
                                                        user.put("name", name);
                                                        user.put("type",typeof);
                                                        user.put("user_id",user_id);
                                                        user.put("phone","");
                                                        db.collection("users")
                                                                .add(user)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        Toast.makeText(RegisterActivity.this, "Added", Toast.LENGTH_LONG).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {

                                                                    }
                                                                });

                                                    }
                                                    else{
                                                        Toast.makeText(RegisterActivity.this, "Could Not send verification Link", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Failed Authentication", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }


}