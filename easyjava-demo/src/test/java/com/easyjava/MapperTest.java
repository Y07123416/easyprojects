package com.easyjava;

import com.easyjava.RunDemoApplication;
import com.easyjava.entity.po.ProductInfo;
import com.easyjava.entity.query.ProductInfoQuery;
import com.easyjava.enums.DateTimePatternEnum;
import com.easyjava.mappers.ProductInfoMapper;
import com.easyjava.service.impl.ProductInfoServiceImpl;
import com.easyjava.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = RunDemoApplication.class)
@RunWith(SpringRunner.class)
public class MapperTest {
    @Resource
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;
    @Test
    public void selectCount(){
        Integer count = productInfoMapper.selectCount(new ProductInfoQuery());
        System.out.println(count);
    }
    @Test
    public void selectList(){
        ProductInfoQuery query = new ProductInfoQuery();
        query.setProductNameFuzzy("小米");
        List<ProductInfo> dataList= productInfoMapper.selectList(query);
        for(ProductInfo product:dataList){
            System.out.print(product);
        }
    }

    @Test
    public void insert(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(4);
        productInfo.setProductName("oppo r17");
        productInfo.setColorType(8);
        productInfo.setCreatetime(new Date());
        productInfoMapper.insert(productInfo);
        System.out.println(productInfo.getId());
    }

    @Test
    public void insertOrUpdate(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setId(5);
        productInfo.setProductName("魅族16");
        productInfo.setColorType(6);
        productInfo.setCreatetime(new Date());
        productInfoMapper.insertOrUpdate(productInfo);
        System.out.println(productInfo);
    }

    @Test
    public void insertBatch(){
        List<ProductInfo> productList = new ArrayList();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("oppo r17");
        productInfo.setColorType(8);
        productInfo.setCreatetime(new Date());
        productList.add(productInfo);
        ProductInfo productInfo1 = new ProductInfo();
        productInfo1.setProductName("ipad pro");
        productInfo1.setColorType(3);
        productInfo1.setCreatetime(new Date());
        productList.add(productInfo1);
        productInfoMapper.insertBatch(productList);
        System.out.println(productInfo);
    }

    @Test
    public void insertOrUpdateBatch(){
        List<ProductInfo> productInfoList = new ArrayList();
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("魅族16");
        productInfo.setColorType(6);
        productInfo.setCreatetime(new Date());
        productInfoList.add(productInfo);
        productInfoMapper.insertOrUpdateBatch(productInfoList);
        System.out.println(productInfo);
    }

    @Test
    public void selectByKey(){
        ProductInfo productInfo = new ProductInfo();
        productInfo = productInfoMapper.selectById(1);
        System.out.println(productInfo);
    }

    @Test
    public void deleteById(){
        Integer de_id =  productInfoMapper.deleteById(11);
        System.out.println(de_id);
    }

    @Test
    public void update(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("小米14");
        productInfo.setColorType(1);
        productInfo.setCreatetime(new Date());
        productInfoMapper.updateById(productInfo,1);
        System.out.println(productInfo);
    }

    @Test
    public void delete(){
        Integer res = productInfoMapper.deleteById(11);
        System.out.println(res);
    }

    @Test
    public void selectbyImpl(){
        ProductInfoServiceImpl impl = new ProductInfoServiceImpl();
        ProductInfo res = impl.getProductInfoById(1);
        System.out.println(res);
    }
}
