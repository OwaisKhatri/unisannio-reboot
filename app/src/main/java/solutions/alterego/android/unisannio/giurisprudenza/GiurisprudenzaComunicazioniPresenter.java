package solutions.alterego.android.unisannio.giurisprudenza;

import org.jsoup.nodes.Document;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import solutions.alterego.android.unisannio.models.Article;

public class GiurisprudenzaComunicazioniPresenter {

    private GiurisprudenzaParser mParser;
    private GiurisprudenzaComunicazioniRetriever mRetriever;
    ArrayList<Article> list = new ArrayList<>();

    public GiurisprudenzaComunicazioniPresenter() {
        this.mParser = new GiurisprudenzaParser();
        this.mRetriever = new GiurisprudenzaComunicazioniRetriever();
    }

    public Observable<ArrayList<Article>> getArticles() {

        return Observable.
                create(new Observable.OnSubscribe<ArrayList<Article>>(){

                    @Override
                    public void call(final Subscriber<? super ArrayList<Article>> subscriber) {

                        mRetriever.get()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Document>() {

                                    @Override
                                    public void onCompleted(){

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Document document) {
                                        list = mParser.parse(document);
                                        subscriber.onNext(list);
                                    }

                                });
                    }
                }).subscribeOn(Schedulers.io());


    }


}