package com.example.demo.shop;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.shop.entity.Products;
import com.example.demo.shop.entity.Stock;
import com.example.demo.shop.repository.ProductsRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductsRepository productsRepository;

    @Override
    public void run(String... args) throws Exception {
        // 若已有商品則不重複建立
        if (productsRepository.count() > 0) {
            System.out.println("✅ 商品資料已存在，跳過初始化");
            return;
        }

        System.out.println("🌱 開始建立預設商品...");

        save("限量小農鮮蔬盒", "生鮮", new BigDecimal("499"),
            "嚴選在地小農友善耕作蔬菜，新鮮現採直送。無毒栽培降低農藥殘留風險，吃得更安心。支持在地農業同時減少長途運輸碳排放。", 50, "/productPictures/VeggieBox.jpg");

        save("【季節限定】柿柿如意鮮果禮盒", "生鮮", new BigDecimal("799"),
            "精選當季新鮮柿子，由在地農民用心栽種。自然熟成風味香甜，無過度人工催熟處理。產地直送縮短運輸流程，保留最佳鮮度。", 30, "/productPictures/persimmon.jpg");

        save("田野直送鮮蔬箱", "生鮮", new BigDecimal("529"),
            "嚴選在地小農每日現採蔬菜，新鮮直送到家，保留最自然的風味與營養。採用友善耕作方式種植，減少農藥與化學肥料使用。", 40, "/productPictures/Veggie.jpg");

        save("旬採鮮果禮盒", "生鮮", new BigDecimal("649"),
            "精選當季成熟水果，由小農自然栽培，無過度催熟與人工加工。果實在最佳熟度採收，保留最純粹甜味與香氣。", 35, "/productPictures/fruit.jpg");

        save("永恆工藝．循環餐具組【流光金】", "日常用品", new BigDecimal("680"),
            "霧面金質感設計，兼具時尚與耐用性，外食或露營都適用。採用可重複使用材質製作，大幅減少一次性餐具浪費。", 60, "/productPictures/gold.jpg");

        save("永恆工藝．循環餐具組【月光銀】", "日常用品", new BigDecimal("580"),
            "俐落極簡外型搭配高耐用金屬材質，長期使用不易損耗。適合上班族、學生與外食族隨身攜帶。", 60, "/productPictures/silver.jpg");

        save("植萃癒合．三效美體護理組(三合一)", "日常用品", new BigDecimal("1880"),
            "結合乳液、精華油與面霜三步驟完整保養。植物來源成分溫和親膚，同時降低對環境負擔。深層滋養與鎖水保濕一次完成。", 25, "/productPictures/cream.jpg");

        save("【天然原萃】手作無添加果糖(減糖)", "食品", new BigDecimal("250"),
            "僅使用純天然水果與零卡替代糖，不含人工色素與防腐劑。口感自然清甜，適合大人小孩安心享用。採用可回收包裝。", 80, "/productPictures/candy.jpg");

        save("【大地秘境】產地直送純淨辛香料", "食品", new BigDecimal("480"),
            "嚴選自永續農法認證在地小農，不噴灑化學農藥。100%純粹，絕不添加防腐劑、人工色素、味精。採用自然風乾與低溫研磨技術。", 70, "/productPictures/spices.jpg");

        save("純淨補水．Tritan 永續運動瓶", "日常用品", new BigDecimal("450"),
            "輕量防漏運動水杯(800ml)，醫療級 Tritan™ 材質，不含雙酚 A (BPA Free)。減少瓶裝水依賴，即便損壞也可回收處理。", 45, "/productPictures/bottle.jpg");

        save("植感定型．天然蠟豆造型髮蠟", "日常用品", new BigDecimal("420"),
            "採用植物蠟與天然油脂配方，好清洗、不殘留，不造成頭皮負擔。不排放難分解化學物質，讓造型也很有環保意識。", 55, "/productPictures/hairWax.jpg");

        save("愛地球環保摺疊紙袋", "日常用品", new BigDecimal("80"),
            "採用環保再生紙材製成，可自然分解，減少塑膠袋對環境造成的負擔。袋身厚實耐用，承重力佳，購物、外出、送禮皆適用。", 200, "/productPictures/bag.jpg");

        System.out.println("✅ 預設商品建立完成");
    }

    private void save(String name, String category, BigDecimal price, String description, int stockQty, String image) {
        Products p = new Products();
        p.setProductName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setProductDescription(description);
        p.setImage(image);

        Stock stock = new Stock();
        stock.setAvailableQuantity(stockQty);
        stock.setProduct(p);
        p.setStock(stock);

        // 先存取得 productId，再設定商品編號
        Products saved = productsRepository.save(p);
        saved.setProductCode(String.format("PRD-%04d", saved.getProductId()));
        productsRepository.save(saved);
    }
}