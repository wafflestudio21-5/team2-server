package com.wafflestudio.team2server.batch

import com.wafflestudio.team2server.common.auth.AuthUserInfo
import com.wafflestudio.team2server.post.model.PostCreateRequest
import com.wafflestudio.team2server.post.service.ProductPostService
import com.wafflestudio.team2server.user.service.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.security.SecureRandom
import kotlin.random.Random

@SpringBootTest
class DummyData @Autowired constructor(
	val postService: ProductPostService,
	val userService: UserService,
) {
	val adjectiveList = listOf(
		"고급스러운",
		"최신형",
		"프리미엄",
		"실속형",
		"인기상품",
		"한정판",
		"특가세일",
		"최저가",
		"초특가",
		"핫딜",
		"무이자",
		"무료배송",
		"슈퍼특가",
		"추천상품",
		"프로모션",
		"할인특가",
		"신제품",
		"베스트셀러",
		"추가할인",
		"럭셔리",
		"휴양지",
		"명품",
		"한방에",
		"올인원",
		"최고급",
		"전문가추천",
		"가을신상",
		"당일배송",
		"초고화질",
		"탁월한",
		"국내제작",
		"폭발세일",
		"수입명품",
		"최고의",
		"편리한",
		"강력추천",
		"파격가",
		"24시간한정",
		"이번주특가",
		"첫구매할인",
		"사은품증정",
		"싸게팔아요",
		"최다구매인기",
		"노련한",
		"전문가인증",
		"눈에띄게",
		"트렌디한",
		"세계적인",
		"오늘만특가"
	)

	val productList = listOf(
		listOf("노트북", "https://www.lge.co.kr/kr/images/notebook/md09877827/gallery/medium13.jpg"),
		listOf("스마트폰", "https://images.samsung.com/kdp/goods/2023/08/03/98f31ad5-b606-4b93-8ed0-5a78af443e7d.png"),
		listOf("카메라", "https://rlyfaazj0.toastcdn.net/20240116/111211.109230000/115952023_1.png"),
		listOf("음향기기", "https://image10.coupangcdn.com/image/retail/images/3728011902782805-0b3ce652-b762-4e8b-9f0c-974f8b0d297c.jpg"),
		listOf("신발", "https://static.nike.com/a/images/c_limit,w_592,f_auto/t_product_v1/3e8455ad-c00c-4996-a85a-b5c4d38c6ae2/v2k-%EB%9F%B0-%EC%97%AC%EC%84%B1-%EC%8B%A0%EB%B0%9C-TeZkXP2L.png"),
		listOf("가전제품", "https://images.samsung.com/kdp/goods/2023/09/19/d7a13891-3045-451b-94b3-ec47687b4b6f.png"),
	)


	private fun toContent(adjective: String, product: String): String {
		return "${adjective} ${product}, \n 이 제품은 ${product}으로 ${adjective} 특징을 가지고 있습니다."
	}

	@Test
	fun insert() {
		val random = SecureRandom()
		val maxAreaId = 3513

		for (areaId in 1001..maxAreaId) {
			println("====================areaId=$areaId============================")
			val user = userService.signup(
				"user$areaId@naver.com",
				"bani123!",
				"사용자$areaId",
				"https://d1unjqcospf8gs.cloudfront.net/assets/home/main/3x/rebranded-image-top-e765d561ee9df7f5ab897f622b8b5a35aaa70314f734e097ea70e6c83bdd73f1.webp",
				listOf(areaId)
			)
			// 랜덤한 이름 100개
			repeat(100) {
				val adjective = adjectiveList[random.nextInt(adjectiveList.size)]
				val product = productList[random.nextInt(productList.size)]
				postService.create(
					PostCreateRequest(
						areaId,
						"$adjective ${product[0]}",
						toContent(adjective, product[0]),
						"TRADE",
						product[1],
						listOf(product[1]),
						false,
						null,
						random.nextInt(99) * 1000,
					),
					AuthUserInfo(
						user.id!!,
						user.refAreaIds.map { it.id },
						false,
						null, null
					)
				)
			}
		}
	}

}

//		listOf("가구", ,
//		"액세서리",
//		"화장품",
//		"건강식품",
//		"도서",
//		"가방",
//		"스포츠용품",
//		"시계",
//		"자동차용품",
//		"식품",
//		"생활용품",
//		"문구",
//		"완구",
//		"헤어제품",
//		"컴퓨터",
//		"운동기구",
//		"애완용품",
//		"주방용품",
//		"캠핑용품",
//		"여행용품",
//		"음료수",
//		"커피머신",
//		"스킨케어",
//		"헬스용품",
//		"키친가전",
//		"모바일액세서리",
//		"가전제품",
//		"휴대폰액세서리",
//		"모자",
//		"양말",
//		"선글라스",
//		"디자이너가방",
//		"캐주얼백",
//		"청바지",
//		"셔츠",
//		"워치",
//		"운동화",
//		"캐주얼신발",
//		"서적",
//		"예술작품",
//		"음악앨범",
//		"운동용품"
