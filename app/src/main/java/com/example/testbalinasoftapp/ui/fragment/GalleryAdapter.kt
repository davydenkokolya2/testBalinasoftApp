import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testbalinasoftapp.databinding.ItemGalleryBinding

data class GalleryItem(val imageUrl: String, val date: String)

class GalleryAdapter(
    private var items: MutableList<GalleryItem>,
    private val onItemClick: (GalleryItem) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItems(newItems: List<GalleryItem>) {
        val currentSize = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(currentSize, newItems.size)
    }

    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GalleryItem) {
            // Загружаем изображение с использованием Picasso или Glide
            Glide.with(binding.imageView.context)
                .load(item.imageUrl)
                .into(binding.imageView)

            // Устанавливаем дату
            binding.dateTextView.text = item.date

            binding.root.setOnClickListener {
                onItemClick(item) // Вызываем обработчик при нажатии
            }
        }
    }
}
