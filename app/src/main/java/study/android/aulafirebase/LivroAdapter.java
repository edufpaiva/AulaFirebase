package study.android.aulafirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;

import java.util.ArrayList;

/**
 * Created by aluno on 10/10/2016.
 */

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.ViewHolder> {
    private OnItemClick callback;
    private ArrayList<Livro> livros;
    private final Context context;

    public LivroAdapter(Context context, ArrayList<Livro> livros) {


        this.context = context;
        this.livros = livros;
    }

    public void setCallback(OnItemClick callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, null);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Livro livro = livros.get(position);
        holder.setTitulo(livro.getTitulo());
        holder.setAutor(livro.getAutor());
        holder.setNPaginas(livro.getPaginas());
        holder.setAno(livro.getAno());
        //holder.setCapa(livro.getCapa());

    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titulo;
        private TextView autor;
        private TextView nPaginas;
        private TextView ano;
        private ImageView capa;


        public ViewHolder(View itemView){
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.nomeLivro);
            autor = (TextView) itemView.findViewById(R.id.nomeAutor);
            nPaginas = (TextView) itemView.findViewById(R.id.nPaginas);
            ano = (TextView) itemView.findViewById(R.id.ano);
            capa = (ImageView) itemView.findViewById(R.id.imgCapa);
            itemView.setOnClickListener(this);
        }


        public void setTitulo(String titulo){
            if (titulo != null){
                this.titulo.setText(titulo);
            }
        }

        public void setAutor(String autor){
            if (autor != null){
                this.autor.setText(autor);
            }
        }

        public void setNPaginas(int nPaginas){
            if (nPaginas != 0){
                this.nPaginas.setText(nPaginas+"");
            }
        }
        public void setAno(int ano){
            if (ano != 0){
                this.ano.setText(ano+"");
            }
        }
        public void setCapa(String capa){

            if(capa == null) return;


            Ocean.glide(context)
                    .load(capa)
                    .build(GlideRequest.BITMAP)
                    .resize(200,250)
                    .into(this.capa);
        }


        @Override
        public void onClick(View view) {


            if (callback != null){
                callback.onItemClick(view, getPosition());

            }
        }


    }


    public interface OnItemClick{
        void onItemClick(View view, int position);

    }
}
