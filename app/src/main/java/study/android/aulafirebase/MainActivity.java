package study.android.aulafirebase;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;

import java.io.File;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements ImageDelegate.BytesListener {

    private EditText edNome, edAutor, edPaginas, edAno;
    private ImageView imgLivro;
    private Spinner sCategoria;
    private Livro livro;
    private File caminhoDaImagem;

    private static final String[] PERMISSIONS_READ_WRITE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int REQUEST_PERMISSION = 3;

    private byte bytesDaImagem[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testeArraySpiner();

        edNome = (EditText) findViewById(R.id.edTitulo);
        edAutor = (EditText) findViewById(R.id.edAutor);
        edAno = (EditText) findViewById(R.id.edAno);
        edPaginas = (EditText) findViewById(R.id.edPaginas);
        sCategoria = (Spinner) findViewById(R.id.spinner);
        imgLivro = (ImageView) findViewById(R.id.imgTopo);
        imgLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamera();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meu_add_livro, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_enviar) {
            salvarLivro();
            //Toast.makeText(this, "Livro Salvo", Toast.LENGTH_LONG).show();


        }


        return super.onOptionsItemSelected(item);
    }


    private void salvarLivro() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://testeaulafirebase.appspot.com").child("livroimages").child(caminhoDaImagem.getName());
        storageRef.putBytes(bytesDaImagem).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Aula",taskSnapshot.getDownloadUrl().toString());

                criaLivro(taskSnapshot.getDownloadUrl().toString());





            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Aula",e.getMessage());
            }
        });




    }


    private void criaLivro(String imgURL){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando Livros...");
        progressDialog.show();

        String titulo = edNome.getText().toString();
        String autor = edAutor.getText().toString();
        int ano = Integer.parseInt(edAno.getText().toString());
        int paginas = Integer.parseInt(edPaginas.getText().toString());
        String categoria = sCategoria.getSelectedItem().toString();


        livro = new Livro(titulo, autor, paginas, ano, categoria, imgURL);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("livros");
        reference.push().setValue(livro).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    limparCampos();

                } else {
                    progressDialog.dismiss();

                }
            }
        });


    }

    private void intentAbrirCamera(){
        String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+"firebase.jpg";
        caminhoDaImagem = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto);

        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(caminhoDaImagem));
        startActivityForResult(it, REQUEST_PERMISSION);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION && resultCode == RESULT_OK){
            if (caminhoDaImagem != null && caminhoDaImagem.exists()){
                Ocean.glide(this)
                        .load(Uri.fromFile(caminhoDaImagem))
                        .build(GlideRequest.BYTES)
                        .addDelegateImageBytes(this)
                        .toBytes(300, 300);
            }else{
                Log.e("Ale","FILE null");
            }
        }else{
            Log.d("Ale","nao usou a camera");
        }
    }

    private void verificaChamarPermissao() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // exibir o motivo de esta precisando da permissao
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_READ_WRITE, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION){
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // tem
                Log.d("Ale","tem permissao");
                intentAbrirCamera();
            } else {
                // nao tem a permissao
                Log.d("Ale","nao tem permissao");
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void limparCampos() {
        edNome.setText("");
        edAutor.setText("");
        edAno.setText("");
        edPaginas.setText("");
            Ocean.
                glide(this).
                load(R.mipmap.ic_launcher).
                build(GlideRequest.BITMAP).
                into(imgLivro);



    }

    private void abrirCamera(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            verificaChamarPermissao();
        } else {
            // tenho permissao, chama a intent de camera
            intentAbrirCamera();
        }
    }



    //    public void testeFirebase(View v){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");
//
//    }

//    public void lerDados(View v){
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//
//
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("TAG", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });
//
//
//
//    }

    void testeArraySpiner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.aplicativos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


    }

    @Override
    public void createdImageBytes(byte[] bytes) {
        bytesDaImagem = bytes;
        Bitmap bitmap = Ocean.byteToBitmap(bytes);
        imgLivro.setImageBitmap(bitmap);

    }

    public abstract static class PermissionUtil {

        public static boolean verifyPermissions(int[] grantResults) {
            // At least one result must be checked.
            if(grantResults.length < 1){
                return false;
            }

            // Verify that each required permission has been granted, otherwise return false.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

    }
}




