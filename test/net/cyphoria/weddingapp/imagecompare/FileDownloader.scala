package net.cyphoria.weddingapp.imagecompare

import java.io.File
import java.net.URL
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.params.{CookiePolicy, ClientPNames}
import org.apache.http.impl.cookie.BasicClientCookie
import org.apache.http.client.CookieStore
import org.openqa.selenium.WebDriver
import scala.collection.JavaConverters._
import org.apache.commons.io.FileUtils
import org.apache.http.cookie.ClientCookie

/**
 *
 * @author Stefan Penndorf <stefan@cyphoria.net>
 */
class FileDownloader(val webdriver: WebDriver) {

  def downloadFile(source: URL, target: File) {
    val client = new DefaultHttpClient()
    client.getParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY)

    mimicCookies(webdriver.manage().getCookies.asScala, client.getCookieStore)

    val content = client.execute(new HttpGet(source.toURI)).getEntity.getContent

    FileUtils.copyInputStreamToFile(content, target)
  }

  def mimicCookies(seleniumCookies: Iterable[org.openqa.selenium.Cookie], store: CookieStore) = {
    seleniumCookies.map(toCommonsCookie).foreach(store.addCookie(_))
  }

  def toCommonsCookie(keks: org.openqa.selenium.Cookie): BasicClientCookie = {
    val cookie = new BasicClientCookie(keks.getName, keks.getValue)
    cookie.setVersion(1)
    cookie.setDomain(keks.getDomain)
    cookie.setPath(keks.getPath)

    cookie.setAttribute(ClientCookie.VERSION_ATTR, "1")
    cookie.setAttribute(ClientCookie.DOMAIN_ATTR, keks.getDomain)
    cookie
  }


}
