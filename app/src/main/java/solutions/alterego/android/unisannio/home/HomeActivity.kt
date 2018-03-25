package solutions.alterego.android.unisannio.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.novoda.simplechromecustomtabs.SimpleChromeCustomTabs
import com.novoda.simplechromecustomtabs.navigation.IntentCustomizer
import com.novoda.simplechromecustomtabs.navigation.NavigationFallback
import kotlinx.android.synthetic.main.activity_home.article_recyclerview
import kotlinx.android.synthetic.main.activity_home.bottom_navigation
import kotlinx.android.synthetic.main.activity_home.home_progressbar
import solutions.alterego.android.unisannio.App
import solutions.alterego.android.unisannio.R
import solutions.alterego.android.unisannio.R.drawable
import solutions.alterego.android.unisannio.R.id
import solutions.alterego.android.unisannio.core.Article
import solutions.alterego.android.unisannio.utils.gone
import solutions.alterego.android.unisannio.utils.visible
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), HomeView {

    @Inject
    lateinit var presenter: HomePresenter

    private val adapter = ArticleListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        App.component(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.home_toolbar))
        supportActionBar?.title = "Ateneo"
        presenter.attach(this)

        article_recyclerview
            .apply {
                layoutManager = LinearLayoutManager(this@HomeActivity)
                adapter = this@HomeActivity.adapter
                val dividerItemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
                dividerItemDecoration.setDrawable(getDrawable(drawable.list_divider))
                addItemDecoration(dividerItemDecoration)
            }

        bottom_navigation
            .setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    id.navigation_ateneo -> {
                        supportActionBar?.title = "Ateneo"

                        adapter.clear()
                    }
                    id.navigation_ingegneria -> {
                        supportActionBar?.title = "Ingegneria"

                        adapter.clear()
                    }
                    id.navigation_scienze -> {
                        supportActionBar?.title = "Scienze"

                        adapter.clear()
                    }
                    id.navigation_giurisprudenza -> presenter.onGiurisprudenzaClicked()
                    id.navigation_economia -> {
                        supportActionBar?.title = "S.E.A"

                        adapter.clear()
                    }
                }
                true
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.action_web_page -> {
                presenter.goToWebsite()
                true
            }
            R.id.action_map -> {
                presenter.goToMap()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    public override fun onResume() {
        super.onResume()
        SimpleChromeCustomTabs.getInstance().connectTo(this)
    }

    public override fun onPause() {
        SimpleChromeCustomTabs.getInstance().disconnectFrom(this)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun clearList() = adapter.clear()

    override fun setTitleToGiurisprudenza() {
        supportActionBar?.title = "Giurisprudenza"
    }

    override fun showProgressbar() = home_progressbar.visible()

    override fun addArticles(articles: List<Article>) = adapter.addArticles(articles)

    override fun hideProgressbar() = home_progressbar.gone()

    override fun openWebsite(website: String) {
        SimpleChromeCustomTabs.getInstance()
            .withFallback(navigationFallback)
            .withIntentCustomizer(intentCustomizer)
            .navigateTo(Uri.parse(website), this@HomeActivity)
    }

    private val navigationFallback = NavigationFallback { url ->
        Toast.makeText(applicationContext, "No web browser found", Toast.LENGTH_SHORT).show()

        val intent = Intent(Intent.ACTION_VIEW)
            .setData(url)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private val intentCustomizer = IntentCustomizer { simpleChromeCustomTabsIntentBuilder ->
        simpleChromeCustomTabsIntentBuilder
            .withToolbarColor(ContextCompat.getColor(applicationContext, R.color.primaryColor))
            .showingTitle()
            .withUrlBarHiding()
            .withDefaultShareMenuItem()
            .withStartAnimations(applicationContext, R.anim.slide_in_right, R.anim.slide_out_left)
            .withExitAnimations(applicationContext, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
}