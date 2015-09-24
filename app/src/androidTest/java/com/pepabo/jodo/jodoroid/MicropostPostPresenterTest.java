package com.pepabo.jodo.jodoroid;

import android.test.AndroidTestCase;

import com.pepabo.jodo.jodoroid.models.APIService;
import com.pepabo.jodo.jodoroid.models.Micropost;

import java.io.File;

import retrofit.mime.TypedFile;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class MicropostPostPresenterTest extends AndroidTestCase{

    APIService mAPIService;
    MicropostPostPresenter mPresenter;
    MicropostPostView mView;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mAPIService = mock(APIService.class);
        mView = mock(MicropostPostView.class);
        mPresenter = new MicropostPostPresenter();
        mPresenter.setView(mView);
    }

    public void testSubmitSuccess() {
        final Micropost micropost = new Micropost();
        final String content = "success";
        final TypedFile image = new TypedFile("mimeType", new File("path"));

        when(mView.getContent()).thenReturn(content);

        when(mView.getAttachment()).thenReturn(image);

        when(mAPIService.createMicropost(content, image)).thenReturn(
                Observable.just(micropost).subscribeOn(Schedulers.io())
        );

        mPresenter.submit();

        verify(mAPIService, timeout(1000)).createMicropost(content, image);
        verify(mView, timeout(1000)).showProgress(true);
        verify(mView, timeout(1000)).finish();
    }

    /*public void testSubmitError() {
        final Throwable e = new Error();
        final String content = "failed";
        final TypedFile image = new TypedFile("mimeType", new File("path"));

        when(mView.getContent()).thenReturn(content);

        when(mView.getAttachment()).thenReturn(image);

        when(mAPIService.createMicropost(content, image)).thenReturn(
                Observable.<Micropost>error(e).subscribeOn(Schedulers.io())
        );

        mPresenter.submit();

        verify(mAPIService, timeout(1000)).createMicropost(content, image);
        verify(mView, timeout(1000)).showProgress(false);
        verify(mView, timeout(1000)).onError(e);
    }*/
}
