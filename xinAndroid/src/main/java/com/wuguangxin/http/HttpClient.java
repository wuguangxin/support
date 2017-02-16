package com.wuguangxin.http;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.wuguangxin.http.business.ResponseHandler;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 简单封装了客户端请求服务器获取/提交数据的方法，如需强大功能，自行修改或更换，如使用AsyncHttpClient
 * 
 * @author wuguangxin
 * @date: 2014-12-30 下午1:54:57
 */
public class HttpClient{
	private static final String TAG = "HttpClient";
	private int connTimeout = 10000;
	private int soTimeout = 10000;
	private String urlEncoding = HTTP.UTF_8; // 默认编码格式
	private static ThreadPoolExecutor mThreadPool = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.DiscardOldestPolicy());
	private static boolean printLog;  // 是否打印日志
	
	private HttpClient(){}

	private static HttpClient mHttpServiceClient = new HttpClient();

	public static HttpClient getInstance(){
		return mHttpServiceClient;
	}

	private static Context mContext;

	public static void init(Context context){
		mContext = context;
	}

	/**
	 * 客户端Cookie
	 */
	public static CookieStore cookieStore;
	
	/**
	 * 是否打印日志
	 * @return
	 */
	public static boolean isPrintLog(){
		return printLog;
	}

	/**
	 * 设置是否打印日志
	 * @param printLog
	 */
	public static void setPrintLog(boolean printLog){
		HttpClient.printLog = printLog;
	}

	/**
	 * 设置连接服务器超时时间
	 * @param connTimeout
	 */
	public void setConnTimeout(int connTimeout){
		this.connTimeout = connTimeout;
	}

	/**
	 * 设置服务器响应时间
	 * @param soTimeout
	 */
	public void setSoTimeout(int soTimeout){
		this.soTimeout = soTimeout;
	}

	/**
	 * 设置编码格式，默认UTF-8
	 * @param urlEncoding
	 */
	public void setUrlEncoding(String urlEncoding){
		this.urlEncoding = urlEncoding;
	}

	/**
	 * GET请求
	 * @param url 服务器地址
	 * @param responseHandler 回调
	 */
	public void get(String url, ResponseHandler responseHandler){
		doGet(url, responseHandler);
	}
	
	/**
	 * POST请求
	 * @param url 服务器地址
	 * @param params 请求参数集合
	 * @param handler 回调
	 */
	public void post(String url, Params params, ResponseHandler handler){
		doPost(url, params, null, handler);
	}
	
	/**
	 * POST请求
	 * @param url 服务器地址
	 * @param params 请求参数集合
	 * @param file 需要上传的文件
	 * @param handler 回调
	 */
	public void post(String url, Params params, File file, ResponseHandler handler){
		doPost(url, params, file, handler);
	}
	
	/*
	 * 发送GET请求
	 */
	final private void doGet(final String url, final ResponseHandler responseHandler){
		HttpTask httpTask = new HttpTask(){
			@Override
			public void run(){
				send(HttpState.START, url, responseHandler);
				try {
					String testStrResponse = responseHandler.getTestData();
					if(!TextUtils.isEmpty(testStrResponse)){
						// 使用测试数据
						send(HttpState.SUCCESS, testStrResponse, responseHandler);
					} else {
						// 使用服务器数据
						HttpGet httpGet = new HttpGet(url);
						HttpResponse response = createHttpClient().execute(httpGet);
						handleResponse(HttpState.SUCCESS, response, responseHandler);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} catch (IOException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} finally {
					send(HttpState.FINISH, null, responseHandler);
				}
			}
		};
		mThreadPool.execute(httpTask);
	}
	
	/*
	 * 发送Post请求
	 */
	final private void doPost(final String url, final Params params, final File file, final ResponseHandler responseHandler){
		HttpTask httpTask = new HttpTask(){
			@Override
			public void run(){
				try {
					String key = String.format("%s?params=%s", url, params.getUrlParams());
					send(HttpState.START, key, responseHandler);
					MultipartEntity mEntity = new MultipartEntity();
					// 普通参数
					if (params != null && !params.isEmpty()) {
						for (Map.Entry<String, Object> entry: params.entrySet()) {
							mEntity.addPart(entry.getKey(), new StringBody((String) entry.getValue()));
						}
					}
					// 文件
					if(file != null && file.exists() && file.isFile()){
						mEntity.addPart("FILE", new FileBody(file));
					}
					HttpPost httpPost = new HttpPost(url);
					httpPost.setEntity(mEntity);
					HttpResponse response = createHttpClient().execute(httpPost);
					handleResponse(HttpState.SUCCESS, response, responseHandler);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} catch (IOException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} finally {
					send(HttpState.FINISH, null, responseHandler);
				}
			}
		};
		mThreadPool.execute(httpTask);
	}
	
	/**
	 * 上传文件
	 * @param url 服务器地址
	 * @param file File文件
	 * @param responseHandler 回调
	 */
	public void upload(final String url, final File file, final ResponseHandler responseHandler){
		upload(url, file, null, responseHandler);
	}
	
	/**
	 * 上传文件
	 * @param url 服务器地址
	 * @param file File文件
	 * @param params 附加参数
	 * @param responseHandler 回调
	 */
	public void upload(final String url, final File file, final Params params, final ResponseHandler responseHandler){
		Log.d(TAG, "POST " + url);
		if(params != null){
			Log.d(TAG, "参数 " + params.toJSONString());
		}
		HttpTask httpTask = new HttpTask(){
			@Override
			public void run(){
				try {
					send(HttpState.START, url, responseHandler);
					MultipartEntity mEntity = new MultipartEntity();
					if (params != null && !params.isEmpty()) {
						for (Map.Entry<String, Object> entry: params.entrySet()) {
							StringBody par = new StringBody((String) entry.getValue());
							mEntity.addPart(entry.getKey(), par);
						}
					}
					if(file != null){
						FileBody fileBody = new FileBody(file);
						mEntity.addPart("FILE", fileBody);
					}
					HttpPost post = new HttpPost(url);
					post.setEntity(mEntity);
					HttpResponse response =  createHttpClient().execute(post);
					handleResponse(HttpState.SUCCESS, response, responseHandler);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} catch (IOException e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e, responseHandler);
				} finally {
					send(HttpState.FINISH, null, responseHandler);
				}
			}
		};
		mThreadPool.execute(httpTask);
	}
	
	/**
	 * 下载文件，使用URLConnection (实用于本服务器下载)
	 * @param url 服务器地址
	 * @param file 目标文件
	 * @param responseHandler 回调
	 * @return
	 */
	public void download(final String url, final File file, final ResponseHandler responseHandler){
		HttpTask httpTask = new HttpTask(){
			@Override
			public void run(){
				FileOutputStream fos = null;
				BufferedInputStream bis = null;
				try {
					send(HttpState.START, url, responseHandler);
					long s = System.currentTimeMillis();
					URL mUrl = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					URLConnection ucon = mUrl.openConnection(); 
					InputStream is = ucon.getInputStream(); 
					bis = new BufferedInputStream(is);
					
					int contentLength = ucon.getContentLength();
					send(HttpState.COUNT, contentLength, responseHandler);
//					File file = new File(FileUtils.getSDCachePath(), Constants.CACHE_DOWN + File.separator + fileName);
					if (file != null && !file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					//=====================================================
			        fos = new FileOutputStream(file);  
		            byte[] buf = new byte[1024 * 100];      
		            long count = 0;
		            int len = 0;  
		            //循环将缓冲区数据写入文件输入流中  
		            long cur = (System.currentTimeMillis() - s);		            
		            while((len=bis.read(buf))!=-1){     
		            	if(responseHandler.isCancelled()){
							send(HttpState.CANCEL, null, responseHandler);
							Log.d(TAG, "download canceled");
							break;
						}
		            	count += len;
		                fos.write(buf,0,len);
		                cur = (System.currentTimeMillis() - s);
		                if(cur < 1){
		                	cur = 1;
		                }
		                long[] data = new long[2];
		                data[0] = count;
		                data[1] = cur;
		                send(HttpState.PROGRESS, data, responseHandler);
		            }
		            Log.d(TAG, "download finish 耗时: " + (System.currentTimeMillis() - s));
		            send(HttpState.SUCCESS, file, responseHandler);
				} catch (Exception e) {
					e.printStackTrace();
					send(HttpState.FAILURE, e.toString(), responseHandler);
				} finally {
					try {
						if(fos != null){
							fos.close();
							fos = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						if(bis != null){
							bis.close();
							bis = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					send(HttpState.FINISH, null, responseHandler);
				}
			}
		};
		mThreadPool.execute(httpTask);
	}
	
	/**
	 * 获取DefaultHttpClient
	 */
	final private DefaultHttpClient createHttpClient(){
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, urlEncoding);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) " + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
		// 超时设置
		ConnManagerParams.setTimeout(params, 1000); // 从连接池中取连接的超时时间
		HttpConnectionParams.setConnectionTimeout(params, connTimeout); // 连接超时
		HttpConnectionParams.setSoTimeout(params, soTimeout); // 请求超时
		// 设置我们的HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		// 使用线程安全的连接管理来创建HttpClient
		return new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
//		return new DefaultHttpClient(params);
	}

	/*
	 * 发送数据到Handler进行处理
	 */
	private void handleResponse(int what, Object obj, ResponseHandler responseHandler){
		if(what == HttpState.SUCCESS){
			HttpResponse response = (HttpResponse) obj;
			if(response != null){
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					try {
						HttpEntity entity = response.getEntity();
						if(entity != null){
							Header contentType = entity.getContentType();
							String value = contentType.getValue();
							// application/json    text/xml    text/html
							if(value.startsWith("text/html")){
								// 应该是重定向了
								send(HttpState.FAILURE, HttpError.ADDRESS_REDIRECTS_EXCEPTION, responseHandler);  
							} else {
								String strResponse = EntityUtils.toString(entity); // 返回的内容
								send(HttpState.SUCCESS, strResponse, responseHandler);
							}
						} 
					} catch (Exception e) {
						e.printStackTrace();
						send(HttpState.FAILURE, HttpError.DATA_PARSER_ERROR, responseHandler);
					}
				} else {
					send(HttpState.FAILURE, HttpError.HTTPHOST_CONNECT_EXCEPTION, responseHandler);
				}
			} else {
				send(HttpState.FAILURE, HttpError.HTTPHOST_CONNECT_EXCEPTION, responseHandler);
			}
		}
	}
	
	private void send(int what, Object obj, ResponseHandler responseHandler){
		if(responseHandler != null){
			responseHandler.sendMessage(Message.obtain(responseHandler, what, obj));
		}
	}
}
