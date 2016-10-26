package study.android.aulafirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaFirebase extends AppCompatActivity {

    ArrayList<Livro> livros = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_firebase);

        recuperarDadosDoFirebase();

    }


    private void recuperarDadosDoFirebase(){

        FirebaseDatabase.getInstance().getReference().child("livros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Livro livro = snapshot.getValue(Livro.class);
                    Log.d("DEBUG",snapshot.getValue().toString());
                    livros.add(livro);
                }
                criarAdapter(livros);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void criarAdapter(ArrayList<Livro> livros) {
        LivroAdapter adapter = new LivroAdapter(this, livros);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.lista);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //adapter.setCallback(this);


    }
}
