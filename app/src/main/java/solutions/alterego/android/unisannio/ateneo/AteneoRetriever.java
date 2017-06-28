package solutions.alterego.android.unisannio.ateneo;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import solutions.alterego.android.unisannio.URLS;
import solutions.alterego.android.unisannio.interfaces.IParser;
import solutions.alterego.android.unisannio.interfaces.IRetriever;
import solutions.alterego.android.unisannio.models.Article;

public class AteneoRetriever implements IRetriever<Document>{

    /*public Observable<List<Article>> getNewsList(final boolean studenti) {
        return Observable
                .create(new Observable.OnSubscribe<List<Article>>() {
                    @Override
                    public void call(Subscriber<? super List<Article>> subscriber) {
                        List<Article> list = get(studenti);
                        subscriber.onNext(list);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<Article> get(boolean studenti) {
        String url;
        IParser parser;
        if (studenti) {
            url = URLS.ATENEO_STUDENTI_NEWS;
            // Al momento il parser e` lo stesso perche` le due pagine sono simili.
            // Teniamo la porta aperta in caso ci fossero cambiamenti al sito.
            parser = new AteneoAvvisiParser();
        } else {
            url = URLS.ATENEO_NEWS;
            parser = new AteneoAvvisiParser();
        }

        List<Article> newsList;
        try {
            Document doc = Jsoup.connect(url).timeout(10 * 1000).get();
            newsList = parser.parse(doc);
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return newsList;
    }*/

    private String urlToRetrieve;
    private List<String> urlsToRetrieve = new ArrayList<>();

    public AteneoRetriever(String url_retrieve){
        this.urlToRetrieve = url_retrieve;

        //Fill the arrayList with all the urls based on the ATENEO_NEWS URL from page 0 to page 41.
        for(int i = 0; i < 42; i++){
            urlsToRetrieve.add(url_retrieve.concat("?page=").concat(String.valueOf(i)));
        }
    }


    @Override
    public Observable<Document> retriveDocument() {
        return Observable
                .create(new Observable.OnSubscribe<Document>() {
                    @Override
                    public void call(Subscriber<? super Document> subscriber) {

                        Document doc = null;
                        try {
                            doc = getDocument();
                        } catch (IOException e) {
                            subscriber.onError(e);
                        }

                        if (doc != null) {
                            subscriber.onNext(doc);
                            //Log.e("RETRIVER",doc.getAllElements().toString());
                            subscriber.onCompleted();
                        }
                    }
                }).subscribeOn(Schedulers.io());

    }
    private void loadList(){
        Observable.from(urlsToRetrieve).
    }

    public Document getDocument() throws IOException {
        return Jsoup.connect(urlToRetrieve).timeout(10 * 1000).userAgent("Mozilla").get();
    }
}
