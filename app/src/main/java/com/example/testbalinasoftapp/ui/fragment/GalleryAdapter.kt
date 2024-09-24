import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testbalinasoftapp.data.models.ImageItem
import com.example.testbalinasoftapp.databinding.ItemGalleryBinding
import com.example.testbalinasoftapp.domain.Unix.convertUnixToDate

class GalleryAdapter(
    private var items: MutableList<ImageItem>,
    private val onItemClick: (ImageItem) -> Unit,
    private val onItemLongClick: (ImageItem) -> Unit
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

    fun updateItems(newItems: List<ImageItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ImageItem) {
            Glide.with(binding.imageView.context)
                .load(item.url)
                .into(binding.imageView)

            binding.dateTextView.text = convertUnixToDate(item.date)

            binding.root.setOnClickListener {
                onItemClick(item)
            }

            binding.root.setOnLongClickListener {
                onItemLongClick(item)
                true
            }
        }
    }
}
