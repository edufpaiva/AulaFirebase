package study.android.aulafirebase;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edNome, edAutor, edPaginas, edAno;
    private Spinner sCategoria;
    private Livro livro;

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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando Livros...");
        progressDialog.show();

        String titulo = edNome.getText().toString();
        String autor = edAutor.getText().toString();
        int ano = Integer.parseInt(edAno.getText().toString());
        int paginas = Integer.parseInt(edPaginas.getText().toString());
        String categoria = sCategoria.getSelectedItem().toString();


        livro = new Livro(titulo, autor, paginas, ano, categoria);

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

    void limparCampos() {
        edNome.setText("");
        edAutor.setText("");
        edAno.setText("");
        edPaginas.setText("");


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


}



