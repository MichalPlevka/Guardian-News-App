package application.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private ArrayList<Article> adapterData;
    private MainActivity mainActivity;

    public ArticleAdapter(MainActivity mainActivity, ArrayList<Article> adapterData) {
        this.mainActivity = mainActivity;
        this.adapterData = adapterData;
    }

    @NonNull
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_main, parent, false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(view, this);
        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        Article article = adapterData.get(position);

        if (StaticVariables.currentClickedItemPosition == position) {
            if (StaticVariables.currentClickedItemPosition == StaticVariables.previousClickedItemPosition) {
                setMasterViewHolder(holder, article);

                // Reset previousClickedItemPosition and currentClickedItemPosition so we can expand the item more than once
                StaticVariables.previousClickedItemPosition = -1;
                StaticVariables.currentClickedItemPosition = -1;

            } else {
                setDetailViewHolder(holder, article);
            }

        } else if (StaticVariables.currentClickedItemPosition != position) {
            setMasterViewHolder(holder, article);
        }

    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    public void setMasterViewHolder(ArticleAdapter.ArticleViewHolder holder, Article article) {
        holder.relativeLayoutDetail.setVisibility(View.GONE);
        holder.relativeLayoutMaster.setVisibility(View.VISIBLE);

        holder.articleTitleMaster.setText(article.getArticle_title());
        holder.sectionNameMaster.setText(article.getSection_name());
        holder.date.setText(UtilMethods.formatDateToString(article.getDate()));
    }

    public void setDetailViewHolder(ArticleAdapter.ArticleViewHolder holder, Article article) {
        holder.relativeLayoutDetail.setVisibility(View.VISIBLE);
        holder.relativeLayoutMaster.setVisibility(View.GONE);

        holder.articleTitleDetail.setText(article.getArticle_title());
    }

    public void openArticleWebsite(String articleUrl) {
        if (!TextUtils.isEmpty(articleUrl)) {
            String url = articleUrl;
            if (!articleUrl.startsWith("http://") && !articleUrl.startsWith("https://")) {
                url = "http://" + articleUrl;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mainActivity.startActivity(intent);
        }

    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayoutDetail;
        RelativeLayout relativeLayoutMaster;

        TextView articleTitleMaster;
        TextView sectionNameMaster;
        TextView date;

        TextView articleTitleDetail;

        public ArticleViewHolder(final View itemView, final ArticleAdapter adapter) {
            super(itemView);

            relativeLayoutMaster = itemView.findViewById(R.id.includeListViewItemMaster);
            relativeLayoutDetail = itemView.findViewById(R.id.includeListViewItemDetail);

            articleTitleMaster = itemView.findViewById(R.id.articleNameMaster);
            sectionNameMaster = itemView.findViewById(R.id.sectionNameMaster);
            date = itemView.findViewById(R.id.date);

            articleTitleDetail = itemView.findViewById(R.id.articleNameDetail);

            Button goToArticleWebSiteButton = itemView.findViewById(R.id.listviewItemDetailButton);

            // Click listener for article button
            goToArticleWebSiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openArticleWebsite(adapterData.get(getAdapterPosition()).getArticleUrl());
                }
            });

            // Click listener for recyclerview item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticVariables.previousClickedItemPosition = StaticVariables.currentClickedItemPosition;
                    StaticVariables.currentClickedItemPosition = getAdapterPosition();
                    adapterData.get(getAdapterPosition()).setClicked(true);

                    adapter.notifyItemChanged(StaticVariables.currentClickedItemPosition);
                    if (StaticVariables.previousClickedItemPosition != StaticVariables.currentClickedItemPosition) {
                        adapter.notifyItemChanged(StaticVariables.previousClickedItemPosition);
                    }
                }
            });
        }
    }
}
