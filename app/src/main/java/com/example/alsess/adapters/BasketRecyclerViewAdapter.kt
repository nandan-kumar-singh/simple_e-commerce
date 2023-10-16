package com.example.alsess.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.BasketFragmentDirections
import com.example.alsess.OnChangeAmount
import com.example.alsess.databinding.BasketFragmentRowBinding
import com.example.alsess.sqlitedaos.BasketSqliteDao
import com.example.alsess.sqlitedatahelpers.BasketSqliteDataHelper
import com.example.alsess.sqlitemodels.SqliteBasketModel


class BasketRecyclerViewAdapter(val context : Context, val onChangeAmount : OnChangeAmount) : RecyclerView.Adapter<BasketRecyclerViewAdapter.BasketVH>() {
    class BasketVH(val viewBinding : BasketFragmentRowBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketVH {
        val view = BasketFragmentRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BasketVH(view)
    }
    override fun getItemCount(): Int {
        val basketDataHelper = BasketSqliteDataHelper(context)
        val titlelist = BasketSqliteDao().getAllBaskets(basketDataHelper)
        return titlelist.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BasketVH, position: Int) {
        //Sqlite gelen veriler alınıp görünümlere aktarılıp görüntülenir
        val basketDataHelper = BasketSqliteDataHelper(context)
        val basketList = BasketSqliteDao().getAllBaskets(basketDataHelper)
        val basket : SqliteBasketModel = basketList.get(position)

        holder.viewBinding.titleText.text = basket.title.replace("'"," ")
        holder.viewBinding.priceText.text = "${basket.price.toFloat() *  basket.count} $"
        holder.viewBinding.countText.text = basket.count.toString()
        Glide.with(context!!).load(basket.imageUrl).into(holder.viewBinding.imageView)

        // detay sayfasına id gönderilir ve bu id sayesinde detayda tıklanan ürün görüntülenir
        holder.viewBinding.cardview.setOnClickListener {
            val navArgs =BasketFragmentDirections.toProductsDetail(basket.id.toInt())
            Navigation.findNavController(it).navigate(navArgs)
        }

        var count = basket.count

        //Sepete ilk veri eklendiğinde eklnene ürünlerin total fiyatı ortaya çıkar
        total()


        // sepeteki ürünleri arttırmak ve total fiyatı bulmak için kullanılır
        holder.viewBinding.increaseButton.setOnClickListener {
            if (count < 20) {
                count++
                BasketSqliteDao().updateBasket(
                    basketDataHelper,
                    basket.id,
                    basket.title,
                    basket.price,
                    basket.imageUrl,
                    count
                )

                total()
                holder.viewBinding.countText.text = count.toString()
                holder.viewBinding.priceText.text = "${basket.price.toFloat() * count} $"
            }
        }

        //sepeteki ürünleri azaltmak ve total fiyatı bulmak için kullanılır
        holder.viewBinding.decreaseButton.setOnClickListener {
            if(count > 1){
                count --
                BasketSqliteDao().updateBasket(basketDataHelper,
                    basket.id,
                    basket.title,
                    basket.price,
                    basket.imageUrl,
                    count)

                total()
            }


            holder.viewBinding.countText.text = count.toString()
            holder.viewBinding.priceText.text = "${basket.price.toFloat() *  count} $"
        }

        //ürünü sepetten silmek için kullanılır
        holder.viewBinding.buttonDelete.setOnClickListener {
            BasketSqliteDao().deleteProduts(basketDataHelper,basket.id)
            total()
            onChangeAmount.onChange("0")
            notifyDataSetChanged()
        }
    }
    fun total(){
        val basketDataHelper = BasketSqliteDataHelper(context)
        val basketList = BasketSqliteDao().getAllBaskets(basketDataHelper)
        var total = 0.0F
        for (i in 0..(basketList.size - 1)) {
            total += basketList.get(i).price.toFloat() * basketList.get(i).count
            onChangeAmount.onChange(total.toString())
        }
    }
}