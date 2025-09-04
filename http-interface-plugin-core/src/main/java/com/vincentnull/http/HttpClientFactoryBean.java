/*
 * Copyright 2013-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vincentnull.http;

import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;
import java.util.Objects;

/**
 * client创建模版
 *
 * @author Vincent
 * @since 2025-02-06
 */
@Data
public class HttpClientFactoryBean
        implements FactoryBean<Object>, InitializingBean, ApplicationContextAware, BeanFactoryAware {

    /***********************************
     * WARNING! Nothing in this class should be @Autowired. It causes NPEs because of some
     * lifecycle race condition.
     ***********************************/

    private static final Log LOG = LogFactory.getLog(HttpClientFactoryBean.class);

    private Class<?> type;

    private Map<String, String> uriVariables;

    private WebHttpClientWrapper webHttpClientWrapper;

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    private final WebClient.Builder defaultWebClientBuilder;

    // For AOT testing
    public HttpClientFactoryBean() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating a FeignClientFactoryBean.");
        }
        defaultWebClientBuilder = WebClient.builder();

    }

    private <T> T createClient(Class<T> serviceType) {
        WebClient.Builder webClientBuilder;
        if (webHttpClientWrapper != null && webHttpClientWrapper.getWebClientBuilder() != null) {
            webClientBuilder = webHttpClientWrapper.getWebClientBuilder();
        } else {
            webClientBuilder = defaultWebClientBuilder;
        }
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        uriBuilderFactory.setDefaultUriVariables(uriVariables);
        WebClient.Builder builder = webClientBuilder.uriBuilderFactory(uriBuilderFactory);
        WebClient webClient = builder.build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(serviceType);
    }

    @Override
    public void afterPropertiesSet() {
    }


    @Override
    public Object getObject() throws Exception {
        return createClient(this.type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
        beanFactory = context;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpClientFactoryBean that = (HttpClientFactoryBean) o;
        return Objects.equals(applicationContext, that.applicationContext)
                && Objects.equals(beanFactory, that.beanFactory)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationContext, beanFactory, type);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
