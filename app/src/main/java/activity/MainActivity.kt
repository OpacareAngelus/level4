package activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.level4.databinding.ActivityMainBinding
import com.example.level4.databinding.FragmentMyProfileBinding
import model.UsersViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}