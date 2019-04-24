package com.app.mintpartners;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ManageCompany extends AppCompatActivity {

    private FirebaseFirestore db;
    String companyId;
    Button button;
    EditText priceEditText;
    EditText infoEditText;
    Company company;
    TextView companyTextView;

    CheckBox checkBoxNails, checkBoxHair, checkBoxMakeup;
    CheckBox checkBoxPedicure, checkBoxManicure, checkBoxFacial;
    CheckBox checkBoxSolarium, checkBoxEyelashes, checkBoxEyebrows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_company);
        companyId = getIntent().getStringExtra("COMPANY_ID_EXTRA");
        Toast.makeText(this, "ID: " + companyId, Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();

        final DocumentReference companyDocumentRef = db.collection("companies").document(companyId);

        checkBoxMakeup = findViewById(R.id.checkBoxMakeup);
        checkBoxHair = findViewById(R.id.checkBoxHair);
        checkBoxNails = findViewById(R.id.checkBoxNails);
        checkBoxPedicure = findViewById(R.id.checkBoxPedicure);
        checkBoxManicure = findViewById(R.id.checkBoxManicure);
        checkBoxFacial = findViewById(R.id.checkBoxFacial);
        checkBoxSolarium = findViewById(R.id.checkBoxSolarium);
        checkBoxEyelashes = findViewById(R.id.checkBoxEyelashes);
        checkBoxEyebrows = findViewById(R.id.checkBoxEyebrows);

        companyTextView = findViewById(R.id.company_text_view);
        infoEditText = findViewById(R.id.info_edit_text);
        priceEditText = findViewById(R.id.price_edit_text);


        if (company == null) {
            db.collection("companies")
                    .document(companyId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (task.isSuccessful()) {
                                company = document.toObject(Company.class).withId(document.getId());
                                companyFetched();
                            } else {
                            }
                        }
                    });
        }


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String infoDescription = infoEditText.getText().toString();
                String prices = priceEditText.getText().toString();

                companyDocumentRef.update(Company.DESCRIPTION_FIELD_NAME,infoDescription);
                companyDocumentRef.update(Company.PRICE_LIST_FIELD_NAME,prices);



                final boolean nailsChecked = checkBoxNails.isChecked();
                final boolean makeupChecked = checkBoxMakeup.isChecked();
                final boolean hairChecked = checkBoxHair.isChecked();
                final boolean pedicureChecked = checkBoxPedicure.isChecked();
                final boolean manicureChecked = checkBoxManicure.isChecked();
                final boolean facialChecked = checkBoxFacial.isChecked();
                final boolean solariumChecked = checkBoxSolarium.isChecked();
                final boolean eyelashesChecked = checkBoxEyelashes.isChecked();
                final boolean eyebrowsChecked = checkBoxEyebrows.isChecked();

                final List<String> categories = new ArrayList<>();

                if(nailsChecked)categories.add(Company.CATEGORY_NAILS);
                if(makeupChecked)categories.add(Company.CATEGORY_MAKEUP);
                if(hairChecked)categories.add(Company.CATEGORY_HAIR);
                if(pedicureChecked)categories.add(Company.CATEGORY_PEDICURE);
                if(manicureChecked)categories.add(Company.CATEGORY_MANICURE);
                if(facialChecked)categories.add(Company.CATEGORY_FACIAL);
                if(solariumChecked)categories.add(Company.CATEGORY_SOLARIUM);
                if(eyelashesChecked)categories.add(Company.CATEGORY_EYELASHES);
                if(eyebrowsChecked)categories.add(Company.CATEGORY_EYEBROWS);

                companyDocumentRef.update(Company.CATEGORIES_ARRAY_NAME,categories);
            }
        });
    }

    private void companyFetched() {
        infoEditText.setText(company.getDescription());
        priceEditText.setText(company.getPrice_list());
        companyTextView.setText(company.getName());


        List<String> categories = company.getCategories();

        if (categories.contains(Company.CATEGORY_EYEBROWS)) {
            checkBoxEyebrows.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_HAIR)) {
            checkBoxHair.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_EYELASHES)) {
            checkBoxEyelashes.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_FACIAL)) {
            checkBoxFacial.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_MANICURE)) {
            checkBoxManicure.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_PEDICURE)) {
            checkBoxPedicure.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_SOLARIUM)) {
            checkBoxSolarium.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_MAKEUP)) {
            checkBoxMakeup.setChecked(true);
        }
        if(categories.contains(Company.CATEGORY_NAILS)) {
            checkBoxNails.setChecked(true);
        }

        button.setVisibility(View.VISIBLE);
    }
}