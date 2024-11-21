package com.example.mymoo.domain.store.seed;

import com.example.mymoo.domain.store.dto.api.Row;
import com.example.mymoo.domain.store.dto.response.MenuDTO;
import com.example.mymoo.domain.store.entity.AddressNew;
import com.example.mymoo.domain.store.entity.AddressOld;
import com.example.mymoo.domain.store.entity.Menu;
import com.example.mymoo.domain.store.entity.Store;
import com.example.mymoo.domain.store.repository.AddressNewRepository;
import com.example.mymoo.domain.store.repository.AddressOldRepository;
import com.example.mymoo.domain.store.repository.MenuRepository;
import com.example.mymoo.domain.store.repository.StoreRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreSeedService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final StoreRepository storeRepository;
    private final AddressNewRepository addressNewRepository;
    private final AddressOldRepository addressOldRepository;
    private final MenuRepository menuRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int All_PAGE = 10;
    private final int PAGE_SIZE = 10;

    @Value("${api.storeData.uri}") String uri;
    @Value("${api.storeData.key}") String key;

    @PostConstruct @Transactional
    public void updateStore(){
        log.info("Storing Table Update Start...");
        List<Store> legecyStores = storeRepository.findAll();
        List<Row> allRows = new ArrayList<>();

        log.info("Receiving Data from OpenAPI Start...");
        for (int i=1; i<=All_PAGE; i++) {
            URI requestUrl = UriComponentsBuilder
                    .fromUriString(uri)
                    .queryParam("key", key)
                    .queryParam("pSize", PAGE_SIZE)
                    .queryParam("pIndex", i)
                    .encode()
                    .build()
                    .toUri();

            String response = restTemplate.getForObject(requestUrl, String.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {

                // optional, but recommended
                // process XML securely, avoid attacks like XML External Entities (XXE)
                factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(response)));

                NodeList rowDocs = doc.getDocumentElement().getElementsByTagName("row");

                for (int j=0; j<rowDocs.getLength();j++){
                    String rowContents = rowDocs.item(j).getTextContent();
                    String[]  rowContent = rowContents.split("\n");
                    Double logt;
                    Double lat;
                    if (rowContent[8].isBlank() || rowContent[9].isBlank()){
                        logt = 0.0;
                        lat = 0.0;
                    }else{
                        logt = Double.parseDouble(rowContent[8]);
                        lat = Double.parseDouble(rowContent[9]);
                    }
                    allRows.add( Row.builder()
                            .name(rowContent[3].substring(4))
                            .adderssOld(rowContent[5])
                            .addressNew(rowContent[6])
                            .zipcode(rowContent[7])
                            .LOGT(logt)
                            .LAT(lat)
                            .build()
                    );
                }
                log.info(i+"th Page recived (DataSize = "+ rowDocs.getLength() +")");

            } catch (ParserConfigurationException | IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> storeImages = new ArrayList<>();
        storeImages.add("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EB%8D%AE%EB%B0%A5%EC%A7%91/store.jpg");
        storeImages.add("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%A4%EB%B0%94%EC%83%81%EB%8F%88%EA%B9%8C%EC%8A%A4/store.jpg");
        storeImages.add("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%AC%EB%8D%B0%EC%9D%B4%ED%8C%8C%EC%8A%A4%ED%83%80/store.jpg\n");
        storeImages.add("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%A7%AC%EB%BD%95%EA%B4%80/store.jpg");
        storeImages.add("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%B0%8C%EA%B0%9C%EB%8D%95%ED%9B%84/store.jpg");

        List<MenuDTO> no1SeedMenus = new ArrayList<>();
        no1SeedMenus.add(MenuDTO.builder()
                .name("김치치킨마요덮밥")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EB%8D%AE%EB%B0%A5%EC%A7%91/%EA%B9%80%EC%B9%98%EC%B9%98%ED%82%A8%EB%A7%88%EC%9A%94%EB%8D%AE%EB%B0%A5_9400.jpg")
                .description("바삭한 치킨과 매콤한 김치, 고소한 마요네즈가 조화를 이루는 인기 덮밥입니다. 남녀노소 누구나 즐길 수 있는 매력적인 맛을 자랑합니다.")
                .price(9400)
                .build());
        no1SeedMenus.add(MenuDTO.builder()
                .name("데리치킨마요덮밥")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EB%8D%AE%EB%B0%A5%EC%A7%91/%EB%8D%B0%EB%A6%AC%EC%B9%98%ED%82%A8%EB%A7%88%EC%9A%94%EB%8D%AE%EB%B0%A5_8900.jpg")
                .description("달콤짭짤한 데리야끼 소스로 맛을 낸 바삭한 치킨과 고소한 마요네즈가 어우러진 덮밥입니다. 감칠맛 나는 조합으로 남녀노소 누구나 좋아하는 메뉴입니다.")
                .price(8900)
                .build());
        no1SeedMenus.add(MenuDTO.builder()
                .name("안동찜닭덮밥")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EB%8D%AE%EB%B0%A5%EC%A7%91/%EC%95%88%EB%8F%99%EC%B0%9C%EB%8B%AD%EB%8D%AE%EB%B0%A5_9900.jpg")
                .description("전통 안동찜닭의 깊고 진한 양념을 덮밥 스타일로 즐길 수 있는 메뉴입니다. 부드러운 닭고기와 감칠맛 나는 간장 베이스 소스가 밥과 함께 어우러져 풍부한 맛을 제공합니다.")
                .price(9900)
                .build());
        no1SeedMenus.add(MenuDTO.builder()
                .name("직화제육덮밥")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EB%8D%AE%EB%B0%A5%EC%A7%91/%EC%A7%81%ED%99%94%EC%A0%9C%EC%9C%A1%EB%8D%AE%EB%B0%A5_11400.jpg")
                .description("직화로 구워낸 제육의 불맛과 매콤달콤한 양념이 어우러진 덮밥입니다. 불향이 가미된 돼지고기와 양념이 밥과 조화롭게 어울려 깊고 진한 맛을 즐길 수 있습니다.")
                .price(11400)
                .build());
        no1SeedMenus.add(MenuDTO.builder()
                .name("춘천닭갈비덮밥")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EB%8D%AE%EB%B0%A5%EC%A7%91/%EC%B6%98%EC%B2%9C%EB%8B%AD%EA%B0%88%EB%B9%84%EB%8D%AE%EB%B0%A5_9900.jpg")
                .description("춘천의 대표 요리인 닭갈비를 덮밥 형태로 즐길 수 있는 메뉴입니다. 매콤하고 달콤한 양념에 잘 볶아진 닭고기와 채소가 밥 위에 올려져 조화로운 맛을 제공합니다. 한 그릇으로도 든든하고 풍미 가득한 한 끼 식사입니다.")
                .price(9900)
                .build());

        List<MenuDTO> no2SeedMenus = new ArrayList<>();
        no2SeedMenus.add(MenuDTO.builder()
                .name("등심까스")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%A4%EB%B0%94%EC%83%81%EB%8F%88%EA%B9%8B%EC%8A%A4/%EB%93%B1%EC%8B%AC%EA%B9%8C%EC%8A%A4_11500.jpg")
                .description("두툼한 돼지 등심을 바삭하게 튀겨낸 일본식 돈가스 요리입니다. 겉은 바삭하고 속은 부드러워 고소한 맛과 육즙이 조화롭게 어우러집니다. 특제 소스와 함께 제공되어 깊은 풍미를 더합니다.")
                .price(11500)
                .build());
        no2SeedMenus.add(MenuDTO.builder()
                .name("생선까스")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%A4%EB%B0%94%EC%83%81%EB%8F%88%EA%B9%8B%EC%8A%A4/%EC%83%9D%EC%84%A0%EA%B9%8C%EC%8A%A4_12000.jpg")
                .description("신선한 생선을 바삭하게 튀겨낸 요리로, 겉은 바삭하고 속은 부드럽고 촉촉한 것이 특징입니다. 담백한 생선의 맛과 함께 제공되는 타르타르 소스나 특제 소스가 조화를 이루어 깔끔하면서도 풍부한 맛을 제공합니다.")
                .price(12000)
                .build());
        no2SeedMenus.add(MenuDTO.builder()
                .name("안심까스")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%A4%EB%B0%94%EC%83%81%EB%8F%88%EA%B9%8B%EC%8A%A4/%EC%95%88%EC%8B%AC%EA%B9%8C%EC%8A%A4_11000.jpg")
                .description("부드러운 돼지 안심 부위를 사용해 만든 돈가스입니다. 육질이 부드럽고 담백하여 겉은 바삭하지만 속은 촉촉한 식감이 특징입니다. 고급스러운 맛과 함께 제공되는 소스가 어우러져 더욱 풍미 있는 한 끼 식사를 제공합니다.")
                .price(11000)
                .build());
        no2SeedMenus.add(MenuDTO.builder()
                .name("치즈까스")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%A4%EB%B0%94%EC%83%81%EB%8F%88%EA%B9%8B%EC%8A%A4/%EC%B9%98%EC%A6%88%EA%B9%8C%EC%8A%A4_12500.jpg")
                .description("돼지 안심이나 등심 속에 고소한 치즈를 넣어 튀겨낸 요리입니다. 겉은 바삭한 튀김옷 안에 녹아내린 치즈가 부드럽게 퍼지며, 고기와 치즈의 조화로운 맛이 특징입니다. 한 입 베어 물었을 때의 치즈의 풍미와 식감이 특별한 만족감을 선사합니다.")
                .price(12500)
                .build());
        no2SeedMenus.add(MenuDTO.builder()
                .name("오바상우동")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%A4%EB%B0%94%EC%83%81%EB%8F%88%EA%B9%8B%EC%8A%A4/%EC%98%A4%EB%B0%94%EC%83%81%EC%9A%B0%EB%8F%99_8000.jpg")
                .description("일본식 우동에 다양한 재료가 들어간 퓨전 요리입니다. 일반적으로 오바상(어머니)처럼 정성스럽게 끓인 진한 국물과 쫄깃한 우동 면이 특징이며, 보통 고기, 해산물, 채소 등이 함께 들어가 맛의 풍미를 더합니다. 간단하면서도 깊은 맛을 자랑하는 따뜻한 국물 요리입니다.")
                .price(8000)
                .build());

        List<MenuDTO> no3SeedMenus = new ArrayList<>();
        no3SeedMenus.add(MenuDTO.builder()
                .name("스파이시크림파스타")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%AC%EB%8D%B0%EC%9D%B4%ED%8C%8C%EC%8A%A4%ED%83%80/%EC%8A%A4%ED%8C%8C%EC%9D%B4%EC%8B%9C%ED%81%AC%EB%A6%BC%ED%8C%8C%EC%8A%A4%ED%83%80_11900.jpg")
                .description("매콤한 맛과 고소한 크림 소스가 어우러진 파스타입니다. 크림의 부드러운 맛에 고추나 다양한 향신료로 매운맛을 더해 풍미가 깊고, 입맛을 돋우는 매운 크림 소스가 특징입니다. 일반적으로 치킨, 새우, 혹은 채소와 함께 제공되어 풍성한 맛을 즐길 수 있습니다.")
                .price(11900)
                .build());
        no3SeedMenus.add(MenuDTO.builder()
                .name("새우알리오올리오파스타")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%AC%EB%8D%B0%EC%9D%B4%ED%8C%8C%EC%8A%A4%ED%83%80/%EC%83%88%EC%9A%B0%EC%A0%A4리오올리오파스타_11500.jpg")
                .description("올리브 오일, 마늘, 고추를 베이스로 한 알리오올리오 소스에 신선한 새우를 더한 파스타입니다. 고소한 올리브 오일과 마늘의 향긋한 맛이 새우와 잘 어우러져 깔끔하면서도 풍미 깊은 맛을 제공합니다. 매콤한 고추가 살짝 매운 맛을 더해 입맛을 돋우는 인기 메뉴입니다.")
                .price(11500)
                .build());
        no3SeedMenus.add(MenuDTO.builder()
                .name("새우로제파스타")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%AC%EB%8D%B0%EC%9D%B4%ED%8C%8C%EC%8A%A4%ED%83%80/%EC%83%88%EC%9A%B0%EB%A1%9C%EC%A0%9C%ED%8C%8C%EC%8A%A4%ED%83%80_11900.jpg")
                .description("부드러운 크림 소스와 토마토 소스가 결합된 로제 소스에 신선한 새우를 더한 파스타입니다. 고소하고 진한 크림과 상큼한 토마토의 조화가 새우와 어우러져 풍미가 깊고, 부드러우면서도 풍성한 맛을 제공합니다. 맛의 균형이 잘 맞아 누구나 좋아할 수 있는 메뉴입니다.")
                .price(11900)
                .build());
        no3SeedMenus.add(MenuDTO.builder()
                .name("베이컨김치필라프")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%AC%EB%8D%B0%EC%9D%B4%ED%8C%8C%EC%8A%A4%ED%83%80/%EB%B2%A0%EC%9D%B4%EC%BB%A8%EA%B9%80%EC%B9%98%ED%95%84%EB%9D%BC%ED%94%84_11900.jpg")
                .description("볶은 베이컨과 매콤한 김치를 고소한 밥과 함께 볶아낸 메뉴입니다. 베이컨의 풍미와 김치의 매콤한 맛이 어우러져 깊은 맛을 자랑하며, 고슬고슬한 밥과 함께 조화롭게 즐길 수 있습니다. 간단하면서도 맛있는 한 끼로 인기가 많은 메뉴입니다.")
                .price(11900)
                .build());
        no3SeedMenus.add(MenuDTO.builder()
                .name("우삼겹필라프")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%98%AC%EB%8D%B0%EC%9D%B4%ED%8C%8C%EC%8A%A4%ED%83%80/%EC%9A%B0%EC%82%BC%EA%B2%B9%ED%95%84%EB%9D%BC%ED%94%84_11900.jpg")
                .description("부드럽고 고소한 우삼겹과 고슬고슬한 밥을 함께 볶아낸 요리입니다. 우삼겹의 진한 육즙과 풍미가 밥에 스며들어 맛이 깊고, 다양한 채소와 함께 볶아 더욱 풍성한 맛을 제공합니다. 간단하면서도 만족스러운 한 끼로, 육류와 밥의 조화가 매력적인 메뉴입니다.")
                .price(11900)
                .build());

        List<MenuDTO> no4SeedMenus = new ArrayList<>();
        no4SeedMenus.add(MenuDTO.builder()
                .name("게살볶음밥")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%A7%AC%EB%BD%95%EA%B4%80/%EA%B2%8C%EC%82%B4%EB%B3%B6%EC%9D%8C%EB%B0%A5_10000.jpg")
                .description("신선한 게살과 고슬고슬한 밥을 함께 볶아 만든 요리입니다. 게살의 달콤하고 담백한 맛이 밥에 스며들어 풍미가 깊고, 다양한 채소와 함께 볶아 색감도 아름답습니다. 간단하지만 맛있는 한 끼로, 해산물의 풍미를 좋아하는 사람들에게 인기가 많은 메뉴입니다.")
                .price(10000)
                .build());
        no4SeedMenus.add(MenuDTO.builder()
                .name("명품짬뽕")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%A7%AC%EB%BD%95%EA%B4%80/%EB%AA%85%ED%92%88%EC%A7%AC%EB%BD%95_11000.jpg")
                .description("깊고 진한 국물 맛이 특징인 고급스러운 짬뽕입니다. 다양한 해산물과 고기, 신선한 채소가 풍성하게 들어가고, 매콤하면서도 감칠맛 나는 국물이 입맛을 돋웁니다. 일반 짬뽕보다 더 진하고 풍부한 맛을 자랑하는 메뉴로, 퀄리티 높은 재료와 정성으로 만든 맛있는 짬뽕입니다.")
                .price(11000)
                .build());
        no4SeedMenus.add(MenuDTO.builder()
                .name("볶음짬뽕")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%A7%AC%EB%BD%95%EA%B4%80/%EB%B3%B6%EC%9D%8C%EC%A7%AC%EB%BD%95_12000.jpg")
                .description("짬뽕의 재료를 볶아서 만든 매운 볶음면 요리입니다. 해산물과 고기, 채소를 매콤하게 볶아낸 후, 쫄깃한 면과 함께 비벼 먹는 스타일로, 국물이 없는 대신 더욱 진한 맛을 느낄 수 있습니다. 볶음짬뽕은 매운 맛과 함께 고소하고 풍부한 맛이 조화를 이루는 인기 있는 메뉴입니다.")
                .price(12000)
                .build());
        no4SeedMenus.add(MenuDTO.builder()
                .name("짜장면")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%A7%AC%EB%BD%95%EA%B4%80/%EC%A7%9C%EC%9E%A5%EB%A9%B4_8000.jpg")
                .description("진한 춘장 소스를 넣어 만든 중국식 면 요리입니다. 고기, 채소, 해산물 등을 춘장에 볶아 고소하고 짭짤한 소스를 면과 함께 비벼 먹는 전통적인 메뉴입니다. 면은 쫄깃하고, 짜장 소스는 깊고 풍부한 맛을 자랑해 누구나 좋아하는 인기 있는 한 그릇 요리입니다.")
                .price(8000)
                .build());
        no4SeedMenus.add(MenuDTO.builder()
                .name("탕수육")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%A7%AC%EB%BD%95%EA%B4%80/%ED%83%95%EC%88%98%EC%9C%A1_13000.jpg")
                .description("바삭하게 튀긴 돼지고기나 소고기 조각에 새콤달콤한 탕수 소스를 끼얹은 중국식 요리입니다. 겉은 바삭하고 속은 부드럽게 튀겨져, 탕수 소스의 달콤하고 시큼한 맛이 조화를 이룹니다. 간식이나 메인 요리로 인기가 많고, 밥과 함께 즐기기 좋은 메뉴입니다.")
                .price(13000)
                .build());

        List<MenuDTO> no5SeedMenus = new ArrayList<>();
        no5SeedMenus.add(MenuDTO.builder()
                .name("제주생고기김치찌개")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%B0%8C%EA%B0%9C%EB%8D%95%ED%9B%84/%EC%A0%9C%EC%A3%BC%EC%83%9D%EA%B3%A0%EA%B8%B0%EA%B9%80%EC%B9%98%EC%B0%8C%EA%B0%9C_13900.jpg")
                .description("제주산 신선한 돼지고기를 듬뿍 넣어 만든 김치찌개입니다. 고기의 풍미와 깊은 맛이 김치와 잘 어우러져 진하고 얼큰한 국물이 특징입니다. 제주도 특유의 신선한 재료를 사용해 깊고 풍부한 맛을 자랑하며, 밥과 함께 먹으면 더욱 맛있습니다.")
                .price(13900)
                .build());
        no5SeedMenus.add(MenuDTO.builder()
                .name("찌개+두루치기")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%B0%8C%EA%B0%9C%EB%8D%95%ED%9B%84/%EC%B0%8C%EA%B0%9C%2B%EB%91%90%EB%A3%A8%EC%B9%98%EA%B8%B0_22500.jpg")
                .description("진한 국물 맛을 자랑하는 찌개와 매콤하게 볶은 두루치기를 결합한 메뉴입니다. 찌개는 깊고 풍부한 국물 맛을 제공하며, 두루치기는 고기와 채소를 매콤하게 볶아낸 요리로, 두 가지 맛의 조화가 매력적입니다. 함께 먹으면 국물과 볶음의 풍미가 어우러져 더욱 맛있는 한 끼가 됩니다.")
                .price(22500)
                .build());
        no5SeedMenus.add(MenuDTO.builder()
                .name("찌개+불고기")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%B0%8C%EA%B0%9C%EB%8D%95%ED%9B%84/%EC%B0%8C%EA%B0%9C%2B%EB%B6%88%EA%B3%A0%EA%B8%B0_21500.jpg")
                .description("진한 국물 맛의 찌개와 달콤하고 고소한 불고기가 결합된 메뉴입니다. 찌개는 깊은 국물 맛을 제공하고, 불고기는 양념된 고기를 볶아낸 요리로, 둘의 조화가 풍미를 더합니다. 국물의 시원함과 불고기의 단짠 매력이 함께 어우러져 맛있고 만족스러운 한 끼를 완성합니다.")
                .price(21500)
                .build());
        no5SeedMenus.add(MenuDTO.builder()
                .name("참치김치찌개")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%B0%8C%EA%B0%9C%EB%8D%95%ED%9B%84/%EC%B0%B8%EC%B9%98%EA%B9%80%EC%B9%98%EC%B0%8C%EA%B0%9C_13900.jpgtb_donation_usagetb_donation_usage")
                .description("참치와 김치를 함께 끓여낸 찌개입니다. 참치의 기름진 맛과 김치의 매콤하고 시큼한 맛이 어우러져 깊고 풍부한 국물 맛을 제공합니다. 간단하면서도 진한 맛이 특징이며, 밥과 함께 먹으면 든든하고 맛있는 한 끼로 즐길 수 있습니다.")
                .price(13900)
                .build());
        no5SeedMenus.add(MenuDTO.builder()
                .name("햄덕후김치찌개")
                .imagePath("https://mymoo.s3.ap-northeast-2.amazonaws.com/menu_images/%EC%B0%8C%EA%B0%9C%EB%8D%95%ED%9B%84/%ED%96%84%EB%8D%95%ED%9B%84%EA%B9%80%EC%B9%98%EC%B0%8C%EA%B0%9C_13900.jpg")
                .description("햄과 김치를 주재료로 한 김치찌개입니다. 햄이 김치와 함께 끓여져 짭짤하고 풍미 가득한 국물이 특징입니다. 김치의 매콤하고 시큼한 맛과 햄의 부드럽고 고소한 맛이 잘 어우러져 간단하지만 깊은 맛을 즐길 수 있는 메뉴입니다.")
                .price(13900)
                .build());

        List<List<MenuDTO>> allSeedMenus = new ArrayList<>();
        allSeedMenus.add(no1SeedMenus);
        allSeedMenus.add(no2SeedMenus);
        allSeedMenus.add(no3SeedMenus);
        allSeedMenus.add(no4SeedMenus);
        allSeedMenus.add(no5SeedMenus);


        log.info("Receiving Data from OpenAPI Complete (" + allRows.size() +" number of Data Received)");
        log.info("Storing New Data Start...");
        int updated = 0;
        int index = 0;
        for (Row row : allRows) {
            boolean isNew = legecyStores.stream().noneMatch(store -> store.getName().equals(row.getName()));
            if (isNew) {
                updated += 1;
                Store newStore = Store.builder()
                        .name(row.getName())
                        .likeCount(0)
                        .allDonation(0L)
                        .usableDonation(0L)
                        .zipCode(row.getZipcode())
                        .address(row.getAddressNew().substring(4))
                        .imagePath(storeImages.get(index%5))
                        .longitude(row.getLOGT())
                        .latitude(row.getLAT())
                        .build();

                storeRepository.save(newStore);

                System.out.println(newStore.getId());

                List<Menu> menuSet = new ArrayList<>();
                for (MenuDTO menu : allSeedMenus.get(index%5)){
                    Menu menuEntity = menu.toEntity();
                    menuEntity.setStore(newStore);
                    menuSet.add(menuEntity);
                }
                index++;

                menuRepository.saveAll(menuSet);

                addressOldRepository.save(AddressOld.convertToAddress(row.getAdderssOld(), newStore));
                addressNewRepository.save(AddressNew.convertToAddress(row.getAddressNew(), newStore));
            }
        }
        log.info(updated + " number of Rows Updating Complete");
        log.info("Storing Table Update Complete");
    }
}
