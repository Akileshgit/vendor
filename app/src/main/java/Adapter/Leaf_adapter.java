package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.bouncycastle.asn1.x509.Holder;

import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Config.SharedPref;
import Model.Product_model;
import util.DatabaseHandler;
import vendor.tcc.MainActivity;
import vendor.tcc.R;

import static android.content.Context.MODE_PRIVATE;


public class Leaf_adapter extends RecyclerView.Adapter<Leaf_adapter.MyViewHolder> {

    private List<Product_model> modelList;
    private Context context;
    private DatabaseHandler dbcart;
    String language;
    /*int quantity;*/
    SharedPreferences preferences;
    HashMap<String, String> map = new HashMap<>();
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_price, tv_min, tv_total, tv_contetiy, tv_add;
        public ImageView iv_logo, iv_plus, iv_minus, iv_remove;
        public Double reward;
        private RelativeLayout mAddRemoveLayout;


        public MyViewHolder(View view) {
            super(view);

            tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
            tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_min = view.findViewById(R.id.tv_subcat_min);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
            tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
            iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
            iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);
            mAddRemoveLayout = (RelativeLayout) view.findViewById(R.id.add_sub_layout);
            iv_remove.setVisibility(View.GONE);
            iv_minus.setOnClickListener(this);
            iv_plus.setOnClickListener(this);
            tv_add.setOnClickListener(this);
            iv_logo.setOnClickListener(this);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            if (id == R.id.iv_subcat_plus) {
                int currentvalue = 0;
                int plusvalue = 0;
                if (tv_contetiy.getText().toString().equals("0")) {
                    currentvalue = Integer.valueOf(modelList.get(position).getMin_value());

                    tv_contetiy.setText(String.valueOf(currentvalue));
                } else {
                    currentvalue = Integer.valueOf(tv_contetiy.getText().toString());
                    plusvalue = Integer.valueOf(modelList.get(position).getPlus());

                    tv_contetiy.setText(String.valueOf(currentvalue + plusvalue));
//                    Log.e("======plus====", ""+String.valueOf(currentvalue + plusvalue));

                }
                //Integer currentvalue= Integer.valueOf(tv_contetiy.getText().toString());


                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");


                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("product_name", modelList.get(position).getProduct_name());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_description", modelList.get(position).getProduct_description());
                map.put("deal_price", modelList.get(position).getDeal_price());
                map.put("start_date", modelList.get(position).getStart_date());
                map.put("start_time", modelList.get(position).getStart_time());
                map.put("end_date", modelList.get(position).getEnd_date());
                map.put("end_time", modelList.get(position).getEnd_time());
                map.put("price", modelList.get(position).getPrice());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("status", modelList.get(position).getStatus());
                map.put("in_stock", modelList.get(position).getIn_stock());
                map.put("unit_value", modelList.get(position).getUnit_value());
                map.put("min_limit", modelList.get(position).getMin_value());
                map.put("unit", modelList.get(position).getUnit());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("rewards", modelList.get(position).getRewards());
                map.put("stock", modelList.get(position).getStock());
                map.put("title", modelList.get(position).getTitle() + "//" + modelList.get(position).getPlus());

                Log.e("===title====", modelList.get(position).getTitle() + "//" + modelList.get(position).getPlus());
//                map.put("plus_limit", modelList.get(position).getPlus());


                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }
                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));

                Double price = Double.parseDouble(map.get("price").trim());

                tv_total.setText("" + price * items);
                updateintent();
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());


            } else if (id == R.id.iv_subcat_minus) {
                int currentvalue = Integer.valueOf(tv_contetiy.getText().toString());
                int plusvalue = Integer.valueOf(modelList.get(position).getPlus());
                //Integer currentvalue= Integer.valueOf(modelList.get(position).getMin_value());
                if (currentvalue > Integer.valueOf(modelList.get(position).getMin_value())) {
                    /*currentvalue--;*/
                    tv_contetiy.setText(String.valueOf(currentvalue - plusvalue));
                    currentvalue -= plusvalue;
                }
                else {
                    tv_contetiy.setText("0");
                    currentvalue = 0;
                }

                if (tv_contetiy.getText().toString().equalsIgnoreCase("0")) {

                    dbcart.removeItemFromCart(modelList.get(position).getProduct_id());
                    //modelList.remove(position);
                    notifyDataSetChanged();

                    updateintent();
                    ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
                }else
                {
                    dbcart.updateQtyByProductId(modelList.get(position).getProduct_id(),String.valueOf(currentvalue));
                    updateintent();
                }
                   /* if (tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                        dbcart.removeItemFromCart(modelList.get(position).getProduct_id());
                        //modelList.remove(position);
                        notifyDataSetChanged();

                        updateintent();
                        ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
                    }else
                    {
                        dbcart.updateQtyByProductId(modelList.get(position).getProduct_id(),String.valueOf(currentvalue));
                        updateintent();
                   }*/


            } else if (id == R.id.iv_subcat_img) {
                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language = preferences.getString("language", "");
                Log.d("lang", language);
                if (language.contains("english")) {
                    showProductDetail(modelList.get(position).getProduct_image(),
                            modelList.get(position).getProduct_name(),
                            modelList.get(position).getProduct_description(),
                            "",
                            position, tv_contetiy.getText().toString());
                } else {
                    showProductDetail(modelList.get(position).getProduct_image(),
                            modelList.get(position).getProduct_name_arb(),
                            modelList.get(position).getProduct_description_arb(),
                            "",
                            position, tv_contetiy.getText().toString());
                }
            }
        }
    }


    public Leaf_adapter(List<Product_model> modelList, Context context) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(context);
    }

    @Override
    public Leaf_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product_rv, parent, false);
        context = parent.getContext();
        return new Leaf_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Leaf_adapter.MyViewHolder holder, int position) {
        Product_model mList = modelList.get(position);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.iv_logo);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

        SharedPreferences.Editor prefEditor =preferences.edit();
        prefEditor.putInt(modelList.get(position).getProduct_name(), Integer.valueOf(modelList.get(position).getPlus() ));
        prefEditor.commit();
//        Log.e( "======", modelList.get(position).getPlus());


        if (language.contains("english")) {
            holder.tv_title.setText(mList.getProduct_name());
        }
        else {
            holder.tv_title.setText(mList.getProduct_name());

        }
       /* holder.tv_contetiy.setText("0");
        quantity= Integer.parseInt(modelList.get(position).getMin_value());*/
        holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getPrice() + context.getResources().getString(R.string.tv_pro_price) + " " + mList.getUnit());
        if (Integer.valueOf(modelList.get(position).getStock())<=0){
            holder.tv_add.setVisibility(View.VISIBLE);
            holder.tv_add.setText(R.string.tv_out);
            holder.tv_add.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_add.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.mAddRemoveLayout.setVisibility(View.INVISIBLE);
            //holder.tv_add.setEnabled(false);
            //holder.iv_minus.setEnabled(false);
            //holder.iv_plus.setEnabled(false);
        }

        else  if (dbcart.isInCart(mList.getProduct_id())) {
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            holder.tv_add.setVisibility(View.GONE);
            holder.mAddRemoveLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mAddRemoveLayout.setVisibility(View.VISIBLE);
            holder.tv_add.setVisibility(View.GONE);
            holder.tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }
        Double items = Double.parseDouble(dbcart.getInCartItemQty(mList.getProduct_id()));
        Double price = Double.parseDouble(mList.getPrice());
        Double reward = Double.parseDouble(mList.getRewards());
        holder.tv_total.setText("" + price * items);
        holder.tv_min.setText("Min"+" "+mList.getMin_value()+" "+mList.getUnit()+"/"+"Qty +"+mList.getPlus()+" "+mList.getUnit());

        Integer currentvalue= Integer.valueOf(modelList.get(position).getMin_value());
        holder.tv_contetiy.setText("0");
        if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
            holder.tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    private void showImage(String image) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.product_image_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image_cancle = (ImageView) dialog.findViewById(R.id.iv_dialog_cancle);
        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_dialog_img);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .crossFade()
                .into(iv_image);

        iv_image_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void showProductDetail(String image, String title, String description, String detail, final int position, String qty) {

        final Dialog dialog = new Dialog(context);
        final HashMap<String, String> map = new HashMap<>();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_detail);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView iv_image = (ImageView) dialog.findViewById(R.id.iv_product_detail_img);
        final ImageView iv_minus = (ImageView) dialog.findViewById(R.id.iv_subcat_minus);
        final ImageView iv_plus = (ImageView) dialog.findViewById(R.id.iv_subcat_plus);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_product_detail_title);
        TextView tv_detail = (TextView) dialog.findViewById(R.id.tv_product_detail);
        final TextView tv_contetiy = (TextView) dialog.findViewById(R.id.tv_subcat_contetiy);
        final TextView tv_add = (TextView) dialog.findViewById(R.id.tv_subcat_add);

        tv_title.setText(title);
        tv_detail.setText(detail);
        tv_contetiy.setText(qty);
        tv_detail.setText(description);

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + image)
                .centerCrop()
                .crossFade()
                .into(iv_image);
        if (Integer.valueOf(modelList.get(position).getStock())<=0){
            tv_add.setText(R.string.tv_out);
            tv_add.setTextColor(context.getResources().getColor(R.color.black));
            tv_add.setBackgroundColor(context.getResources().getColor(R.color.gray));
            tv_add.setEnabled(false);
            iv_minus.setEnabled(false);
            iv_plus.setEnabled(false);
        }

        else if (dbcart.isInCart(modelList.get(position).getProduct_id())) {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
            tv_contetiy.setText(dbcart.getCartItemQty(modelList.get(position).getProduct_id()));
        } else {
            tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
        }

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
                language=preferences.getString("language","");

                map.put("product_id", modelList.get(position).getProduct_id());
                map.put("product_name", modelList.get(position).getProduct_name());
                map.put("category_id", modelList.get(position).getCategory_id());
                map.put("product_description", modelList.get(position).getProduct_description());
                map.put("deal_price", modelList.get(position).getDeal_price());
                map.put("start_date", modelList.get(position).getStart_date());
                map.put("start_time", modelList.get(position).getStart_time());
                map.put("end_date", modelList.get(position).getEnd_date());
                map.put("end_time", modelList.get(position).getEnd_time());
                map.put("price", modelList.get(position).getPrice());
                map.put("min_limit", modelList.get(position).getMin_value());
                map.put("product_image", modelList.get(position).getProduct_image());
                map.put("status", modelList.get(position).getStatus());
                map.put("in_stock", modelList.get(position).getIn_stock());
                map.put("unit_value", modelList.get(position).getUnit_value());
                map.put("unit", modelList.get(position).getUnit());
                map.put("increament", modelList.get(position).getIncreament());
                map.put("rewards", modelList.get(position).getRewards());
                map.put("stock", modelList.get(position).getStock());
                map.put("title", modelList.get(position).getTitle());
//                map.put("plus_limit", modelList.get(position).getPlus());


                if (!tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    if (dbcart.isInCart(map.get("product_id"))) {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    } else {
                        dbcart.setCart(map, Float.valueOf(tv_contetiy.getText().toString()));
                        tv_add.setText(context.getResources().getString(R.string.tv_pro_update));
                    }
                } else {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    tv_add.setText(context.getResources().getString(R.string.tv_pro_add));
                }

                Double items = Double.parseDouble(dbcart.getInCartItemQty(map.get("product_id")));
                Double price = Double.parseDouble(map.get("price"));
                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());

                notifyItemChanged(position);

            }
        });

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.valueOf(tv_contetiy.getText().toString());
                qty = qty + 1;

//                tv_contetiy.setText(String.valueOf(qty));
//                ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
//
//                notifyItemChanged(position);
                updateintent();

            }
        });

        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = 0;
                if (!tv_contetiy.getText().toString().equalsIgnoreCase(""))
                    qty = Integer.valueOf(tv_contetiy.getText().toString());

                if (qty > 0) {
                    qty = qty - 1;
                    tv_contetiy.setText(String.valueOf(qty));
                }

                if (tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                    dbcart.removeItemFromCart(map.get("product_id"));
                    modelList.remove(position);
                    notifyDataSetChanged();

                    updateintent();
                }
                else{
//                    ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
//
//                    notifyItemChanged(position);
//                    updateintent();
                }
            }
        });

    }

    private void updateintent() {
        Intent updates = new Intent("Grocery_cart");
        updates.putExtra("type", "update");
        context.sendBroadcast(updates);
    }

}