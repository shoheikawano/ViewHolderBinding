package com.shaunkawano.viewholderbinding;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

@AdapterBinding class CatAdapter extends RecyclerView.Adapter {

  private List<CatViewType> catViewTypeList;

  CatAdapter(List<CatViewType> catViewTypeList) {
    this.catViewTypeList = catViewTypeList;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    return CatAdapterViewHolderBinding.createViewHolder(inflater, parent, viewType);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    CatAdapterViewHolderBinding.bindViewHolder(this, holder, position);
  }

  @Override public int getItemViewType(int position) {
    return CatViewType.of(position).getLayoutResId();
  }

  @Override public int getItemCount() {
    return catViewTypeList.size();
  }

  @ViewHolder(layout = R.layout.row_taro) static class TaroViewHolder
      extends RecyclerView.ViewHolder {

    TextView taroText;
    ImageView taroImage;

    TaroViewHolder(View itemView) {
      super(itemView);
      taroText = (TextView) itemView.findViewById(R.id.taro_text);
      taroImage = (ImageView) itemView.findViewById(R.id.taro_image);
    }

    @OnBind void onBind() {
      taroText.setText(R.string.text_taro);
    }
  }

  @ViewHolder(layout = R.layout.row_shiro) static class ShiroViewHolder
      extends RecyclerView.ViewHolder {

    TextView shiroText;
    ImageView shiroImage;

    ShiroViewHolder(View itemView) {
      super(itemView);
      shiroText = (TextView) itemView.findViewById(R.id.shiro_text);
      shiroImage = (ImageView) itemView.findViewById(R.id.shiro_image);
    }

    @OnBind void onBind() {
      shiroText.setText(R.string.text_shiro);
    }
  }

  @ViewHolder(layout = R.layout.row_leona) static class LeonaViewHolder
      extends RecyclerView.ViewHolder {

    TextView leonaText;
    ImageView leonaImage;

    LeonaViewHolder(View itemView) {
      super(itemView);
      leonaText = (TextView) itemView.findViewById(R.id.leona_text);
      leonaImage = (ImageView) itemView.findViewById(R.id.leona_image);
    }

    @OnBind void onBind(int position) {
      leonaText.setText(R.string.text_leona);
    }
  }

  @ViewHolder(layout = R.layout.row_title) static class TitleViewHolder
      extends RecyclerView.ViewHolder {

    TitleViewHolder(View itemView) {
      super(itemView);
    }

    @OnBind void onBind() {
      ((TextView) itemView).setText(R.string.title);
    }
  }
}
