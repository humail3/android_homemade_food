package uo.nexton.homemadefood.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import uo.nexton.homemadefood.R;
import uo.nexton.homemadefood.activities.LoginActivity;
import uo.nexton.homemadefood.adapters.SliderAdapterExample;
import uo.nexton.homemadefood.models.SliderItem;
import uo.nexton.homemadefood.models.Users;

public class ProfileFragment extends Fragment {

    View root;
    ImageView userProfileImage_Iv;
    FirebaseUser firebaseUser;
    String Uid;
    TextView userProfileName_Tv;
    Button logout_Btn, userEditProfileDetails_Btn, userUpdatePassword_Btn, userShippingAddress_Btn, userChangeAppTheme_Btn,
            userHelp_Btn, userAboutUs_Btn, userReportBug_Btn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    private SliderAdapterExample adapter;
    ImageView editProfilePic_iv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        getProfileDetails();

        firebaseDatabase = FirebaseDatabase.getInstance("https://homemadefood-fee74-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        if (firebaseUser != null) {
            Uid = firebaseUser.getUid();
        }

        SliderView sliderView = root.findViewById(R.id.profileDiscountSlider);
        adapter = new SliderAdapterExample(getContext().getApplicationContext());

        SliderItem sliderItem1 = new SliderItem();
        sliderItem1.setImageUrl("https://firebasestorage.googleapis.com/v0/b/foodator-99739.appspot.com/o/sliders%2Fslider_1_profile-01.png?alt=media&token=3c8d1f3b-fba5-41e4-8a5a-933b2685c926");
        adapter.addItem(sliderItem1);

        SliderItem sliderItem2 = new SliderItem();
        sliderItem2.setImageUrl("https://firebasestorage.googleapis.com/v0/b/foodator-99739.appspot.com/o/sliders%2Fslider_1_profile-02.png?alt=media&token=13e70898-c625-403f-91dc-5079296e038c");
        adapter.addItem(sliderItem2);

        SliderItem sliderItem3 = new SliderItem();
        sliderItem3.setImageUrl("https://firebasestorage.googleapis.com/v0/b/foodator-99739.appspot.com/o/sliders%2Fslider_1_profile-03.png?alt=media&token=68557ad7-178a-4ad4-bb4d-52019d06e316");
        adapter.addItem(sliderItem3);

        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        userProfileImage_Iv = root.findViewById(R.id.userProfileImage_Iv);
        userProfileName_Tv = root.findViewById(R.id.userProfileName_Tv);
        logout_Btn = root.findViewById(R.id.logout_Btn);
        userEditProfileDetails_Btn = root.findViewById(R.id.userEditProfileDetails_Btn);
        userUpdatePassword_Btn = root.findViewById(R.id.userUpdatePassword_Btn);
        userShippingAddress_Btn = root.findViewById(R.id.userShippingAddress_Btn);
        userChangeAppTheme_Btn = root.findViewById(R.id.userChangeAppTheme_Btn);
        userHelp_Btn = root.findViewById(R.id.userHelp_Btn);
        userAboutUs_Btn = root.findViewById(R.id.userAboutUs_Btn);
        userReportBug_Btn = root.findViewById(R.id.userReportBug_Btn);

        logout_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth User = FirebaseAuth.getInstance();
                final AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
                builder.setTitle("Logout");
                builder.setMessage("Do you want to Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        User.signOut();
                        Intent intent = new Intent(getActivity().getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().getFragmentManager().popBackStack();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }

        });

        userEditProfileDetails_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder editProfileAlert = new AlertDialog.Builder(container.getContext());
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                View editProfileView = inflater.inflate(R.layout.edit_profile_dialog_box, null);
                editProfileAlert.setView(editProfileView);
                AlertDialog dialog = editProfileAlert.create();

                dialog.show();

                LinearLayout uploadProfile = editProfileView.findViewById(R.id.uploadProfile_ll);
                editProfilePic_iv = editProfileView.findViewById(R.id.editProfilePic_iv);
                TextInputLayout fullName_ed = editProfileView.findViewById(R.id.fullName_update_ed);

                Button cancelBtn = editProfileView.findViewById(R.id.cancel_btn);
                Button updateBtn = editProfileView.findViewById(R.id.update_btn);

                if (firebaseUser != null) {
                    databaseReference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users userData = snapshot.getValue(Users.class);
                            String fullName = userData.getUserName();
                            fullName_ed.getEditText().setText(fullName);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                uploadProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent openImages = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openImages,1000);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String
                                fullName = fullName_ed.getEditText().getText().toString().trim();

                        if (!fullName.isEmpty()) {
                            fullName_ed.setError(null);
                            fullName_ed.setErrorEnabled(false);

                                databaseReference.child(Uid).child("userName").setValue(fullName);
                                if (firebaseUser != null) {
                                    databaseReference.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Users userData = snapshot.getValue(Users.class);
                                            String fullName = userData.getUserName();
                                            userProfileName_Tv.setText(fullName);
                                            StorageReference imageRef = storageReference.child(Uid);
                                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Glide.with(getActivity().getBaseContext())
                                                            .load(uri)
                                                            .transforms(new CircleCrop())
                                                            .into(userProfileImage_Iv);
                                                }
                                            });
                                            Toast.makeText(getActivity().getBaseContext(), "Profile Updated", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                dialog.dismiss();

                            } else {
                            if (fullName.isEmpty()) {
                                fullName_ed.setError("Name missing");
                            }
                        }
                    }
                });

                fullName_ed.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() != 0) {
                            fullName_ed.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });

        userUpdatePassword_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(container.getContext());
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                View editPasswordView = inflater.inflate(R.layout.update_change_password_dialog, null);
                alertDialog.setView(editPasswordView);
                AlertDialog dialog = alertDialog.create();

                dialog.show();

                TextInputLayout
                        oldPasswordUpdate_ed = editPasswordView.findViewById(R.id.oldPasswordUpdate_ed),
                        newPasswordUpdate_ed = editPasswordView.findViewById(R.id.newPasswordUpdate_ed);
                Button
                        updatePassword = editPasswordView.findViewById(R.id.update_btn),
                        cancelDialog = editPasswordView.findViewById(R.id.cancel_btn);

                ProgressDialog progressDialog = new ProgressDialog(container.getContext());
                progressDialog.setMessage("Updating Password");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                updatePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String
                                oldPassword = oldPasswordUpdate_ed.getEditText().getText().toString().trim(),
                                newPassword = newPasswordUpdate_ed.getEditText().getText().toString().trim();

                        if (!oldPassword.isEmpty()) {
                            oldPasswordUpdate_ed.setError(null);
                            oldPasswordUpdate_ed.setErrorEnabled(false);
                            if (!newPassword.isEmpty()) {
                                newPasswordUpdate_ed.setError(null);
                                newPasswordUpdate_ed.setErrorEnabled(false);
                                if (newPassword.length() >= 8) {
                                    newPasswordUpdate_ed.setError(null);
                                    newPasswordUpdate_ed.setErrorEnabled(false);

                                    progressDialog.show();
                                    if(firebaseUser!=null){
                                        firebaseUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                databaseReference.child(Uid).child("userPassword").setValue(newPassword);
                                                Toast.makeText(getContext().getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }


                                } else {
                                    newPasswordUpdate_ed.setError("Password should be 8 characters or more");
                                }
                            } else {
                                if (oldPassword.isEmpty()) {
                                    oldPasswordUpdate_ed.setError("Old Password Missing");
                                }
                                if (newPassword.isEmpty()) {
                                    newPasswordUpdate_ed.setError("New Password Missing");
                                }
                            }
                        } else {
                            if (oldPassword.isEmpty()) {
                                oldPasswordUpdate_ed.setError("Old Password Missing");
                            }
                            if (newPassword.isEmpty()) {
                                newPasswordUpdate_ed.setError("New Password Missing");
                            }
                        }
                    }
                });

                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        userShippingAddress_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(container.getContext());
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                View editShippingView = inflater.inflate(R.layout.shipping_detials_dialog, null);
                alertDialog.setView(editShippingView);
                AlertDialog dialog = alertDialog.create();

                dialog.show();

                TextInputLayout
                        fullName_shipping_ed = editShippingView.findViewById(R.id.fullName_shipping_ed),
                        phoneNumber_shipping_ed = editShippingView.findViewById(R.id.phoneNumber_shipping_ed),
                        address_shipping_ed = editShippingView.findViewById(R.id.address_shipping_ed),
                        city_shipping_ed = editShippingView.findViewById(R.id.city_shipping_ed);
                Button
                        updateShippingDialog = editShippingView.findViewById(R.id.update_shipping_btn),
                        cancelShippingDialog = editShippingView.findViewById(R.id.cancel_shipping_btn);

                updateShippingDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String
                                fullName = fullName_shipping_ed.getEditText().getText().toString().trim(),
                                phoneNumber = phoneNumber_shipping_ed.getEditText().getText().toString().trim(),
                                address = address_shipping_ed.getEditText().getText().toString().trim(),
                                city = city_shipping_ed.getEditText().getText().toString().trim();

                        if(!fullName.isEmpty()){
                            fullName_shipping_ed.setErrorEnabled(false);
                            fullName_shipping_ed.setError(null);
                            if(!phoneNumber.isEmpty()){
                                phoneNumber_shipping_ed.setErrorEnabled(false);
                                phoneNumber_shipping_ed.setError(null);
                                if(!address.isEmpty()){
                                    address_shipping_ed.setErrorEnabled(false);
                                    address_shipping_ed.setError(null);
                                    if(!city.isEmpty()){
                                        city_shipping_ed.setErrorEnabled(false);
                                        city_shipping_ed.setError(null);

                                        if (firebaseUser != null) {
                                            Users users = new Users(fullName, firebaseUser.getEmail(), phoneNumber, address, city);
                                            databaseReference.child(Uid).child("shippingAddress").setValue(users);
                                            Toast.makeText(getContext().getApplicationContext(),"Information Updated Successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();



                                    }else{
                                        city_shipping_ed.setError("City Missing");
                                    }
                                }else{
                                    address_shipping_ed.setError("Address Missing");
                                }
                            }else{
                                phoneNumber_shipping_ed.setError("Phone Missing");
                            }

                        }else{
                            fullName_shipping_ed.setError("Full Name Missing");
                        }
                    }
                });

                cancelShippingDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Uri imageUri = data.getData();
            uploadImageTOFirebase(imageUri);
        }
    }

    private void uploadImageTOFirebase(Uri imageUri) {

        ProgressDialog progressDialog = new ProgressDialog(getContext().getApplicationContext());
        progressDialog.setMessage("Uploading Image");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uid = user.getUid();
        StorageReference rf = storageReference.child(Uid);
        rf.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                rf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getActivity().getBaseContext())
                                .load(uri)
                                .transforms(new CircleCrop())
                                .into(editProfilePic_iv);

                        progressDialog.dismiss();
                    }
                });
                Toast.makeText(getActivity().getBaseContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void getProfileDetails() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Uid = firebaseUser.getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Name = snapshot.child("userName").getValue().toString();
                    userProfileName_Tv.setText(Name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}