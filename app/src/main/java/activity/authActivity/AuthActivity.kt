package activity.authActivity

import activity.mainActivity.MainActivity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.level4.R
import com.example.level4.databinding.ActivityAuthBinding
import util.ParsingEMailToName

class AuthActivity : AppCompatActivity() {

    private lateinit var sPref: SharedPreferences
    private lateinit var users: SharedPreferences

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        users = getSharedPreferences(
            getString(R.string.users),
            MODE_PRIVATE
        )

        setListeners()
        loadFieldText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.email), binding.etEmail.text.toString())
        outState.putString(getString(R.string.password), binding.etPassword.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        with(binding) {
            etEmail.setText(savedInstanceState.getString(getString(R.string.email), ""))
            etPassword.setText(savedInstanceState.getString(getString(R.string.password), ""))
        }
    }

    override fun onStop() {
        super.onStop()
        if (binding.cbRemember.isChecked) saveFieldText()
    }

    private fun setListeners() {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        val intent = Intent(this, MainActivity::class.java)
        with(binding) {
            btnRegistration.setOnClickListener {
                regUser(intent)
            }
            tvSignIn.setOnClickListener {
                checkPass(intent)
            }
        }
    }

    private fun checkPass(intent: Intent) {
        if (!allChecks())
            return
        val userName = ParsingEMailToName.parseMail(binding.etEmail.text.toString())
        val userData = users.getString(userName, "")?.split("&")?.last()
        if (userData.equals(binding.etPassword.text.toString())) {
            intent.putExtra(getString(R.string.name), userName)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        } else {
            binding.etPassword.error = getString(R.string.incorrect_password)
            return
        }
    }

    private fun regUser(intent: Intent) {
        if (emailCheck() && passwordCheck()) {
            val userName = ParsingEMailToName.parseMail(binding.etEmail.text.toString())
            val ed: SharedPreferences.Editor = users.edit()
            val validTest = users.getString(userName, "")?.split("&")?.first().toString()
            if (validTest == binding.etEmail.text.toString()) {
                binding.etEmail.error =
                    getString(R.string.this_mail_using)
                return
            } else {
                ed.putString(userName, "${binding.etEmail.text}&${binding.etPassword.text}")
                ed.apply()
                intent.putExtra(getString(R.string.name), userName)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        } else {
            allChecks()
            return
        }
    }

    private fun allChecks(): Boolean {
        with(binding) {
            if (!emailCheck() && !passwordCheck()) {
                etEmail.error = getString(R.string.invalid_email)
                etPassword.error = getString(R.string.invalid_password)
                return false
            }
            //When e-mail didn't used before.
            if (!emailCheck() && passwordCheck()) {
                etEmail.error = getString(R.string.invalid_email)
                return false
            }
            //When password is wrong for this e-mail.
            if (emailCheck() && !passwordCheck()) {
                etPassword.error = getString(R.string.invalid_password)
                return false
            }
        }

        return true
    }

    private fun passwordCheck(): Boolean {
        return binding.etPassword.text.isNotEmpty()
    }

    private fun emailCheck(): Boolean {
        return with(binding) {
            etEmail.text.contains("@") &&
            etEmail.text.contains(".") &&
            etEmail.text.split("@").first().contains(".") &&
            etEmail.text.isNotEmpty()
        }
    }

    private fun saveFieldText() {
        sPref = getPreferences(MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sPref.edit()
        ed.putString(getString(R.string.email), binding.etEmail.text.toString())
        ed.putString(getString(R.string.password), binding.etPassword.text.toString())
        ed.apply()
    }

    private fun loadFieldText() {
        sPref = getPreferences(MODE_PRIVATE)
        with(binding) {
            etEmail.setText(sPref.getString(getString(R.string.email), ""))
            etPassword.setText(sPref.getString(getString(R.string.password), ""))
        }
    }
}

