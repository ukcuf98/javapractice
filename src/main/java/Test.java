import entity.TestVo;

/**
 * @Description:
 * @author: Lucifer
 * @date: 2016/8/30 17:04
 */
public class Test {

    public static void main(String[] args) {
        //不用new关键词创建一个类
        //方法1:Using newInstance() Method
        try {
            Class testVoClass = Class.forName("entity.TestVo");
            TestVo testVo = (TestVo) testVoClass.newInstance();
            System.out.println(testVo.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //方法2:clone
//        TestVo test1 = new TestVo();
//        TestVo test2 = test1.clone();
        //


//        /** * 城市 ES 业务逻辑实现类 * * Created by bysocket on 07/02/2017. */
//        @Servicepublic
//        class CityESServiceImpl implements CityService {
//            private static final Logger LOGGER = LoggerFactory.getLogger(CityESServiceImpl.class);
//            @Autowired
//            CityRepository cityRepository;
//
//            @Override
//            public Long saveCity(City city) {
//                City cityResult = cityRepository.save(city);
//                return cityResult.getId();
//            }
//
//            @Override
//            public List<City> searchCity(Integer pageNumber, Integer pageSize, String searchContent) {// 分页参数Pageable pageable = new PageRequest(pageNumber, pageSize);
//                // Function Score
//                QueryFunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
//                        .add(
//                                QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("cityname", searchContent)), ScoreFunctionBuilders.weightFactorFunction(1000)
//                        )
//                        .add(
//                                QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("description", searchContent)), ScoreFunctionBuilders.weightFactorFunction(100)
//                        );
//                // 创建搜索 DSL 查询
//                SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(functionScoreQueryBuilder).build();
////                LOGGER.info("/n searchCity(): searchContent [" + searchContent + "] /n DSL  = /n " + searchQuery.getQuery().toString());
//                Page<City> searchPageResults = cityRepository.search(searchQuery);
//                return searchPageResults.getContent();
//            }
//        }
    }
}
