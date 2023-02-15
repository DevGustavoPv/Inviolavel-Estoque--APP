package com.devgusta.inviolavelestoque.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devgusta.inviolavelestoque.R;
import com.devgusta.inviolavelestoque.model.Produtos;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProdutos extends RecyclerView.Adapter<AdapterProdutos.MyViewHolder> {
    private List<Produtos> list;
    private setClick setClick;

    public AdapterProdutos(List<Produtos> list, setClick setClick) {
        this.list = list;
        this.setClick = setClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produtos_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produtos produtos = list.get(position);
        holder.titulo.setText(produtos.getTitulo());
        holder.unidades.setText(produtos.getUnidades());
        holder.valor.setText("R$ "+produtos.getValor());
        Picasso.get().load(produtos.getUrlImg()).into(holder.imgProduto);

        //click
        holder.itemView.setOnClickListener(v -> setClick.onClickListener(produtos));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
   public interface setClick{
        public void onClickListener(Produtos produtos);
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo,unidades,valor;
        private ImageView imgProduto;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.editNomeProduto);
            unidades = itemView.findViewById(R.id.editQtdProdutos);
            valor = itemView.findViewById(R.id.editValorProduto);
            imgProduto = itemView.findViewById(R.id.imgProduct);
        }
    }
}
