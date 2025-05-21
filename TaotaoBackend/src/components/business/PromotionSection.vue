<template>
  <div class="promotion-section">
    <div class="section-header">
      <h2>促销活动</h2>
    </div>
    <div class="promotions-flex">
      <div class="promotion-card big" @click="handlePromotionClick(promotions[0])">
        <div class="promo-img" :style="{ backgroundImage: `url(${promotions[0].background})` }"></div>
        <div class="promotion-content">
          <div class="promotion-tag" :class="promotions[0].tagType">{{ promotions[0].tag }}</div>
          <h3>{{ promotions[0].title }}</h3>
          <p>{{ promotions[0].description }}</p>
          <el-button :style="btnStyle">立即抢购</el-button>
        </div>
      </div>
      <div class="promotion-side">
        <div v-for="promotion in promotions.slice(1,3)" :key="promotion.id" class="promotion-card small" @click="handlePromotionClick(promotion)">
          <div class="promo-img" :style="{ backgroundImage: `url(${promotion.background})` }"></div>
          <div class="promotion-content">
            <div class="promotion-tag" :class="promotion.tagType">{{ promotion.tag }}</div>
            <h3>{{ promotion.title }}</h3>
            <p>{{ promotion.description }}</p>
            <el-button :style="btnStyle">立即抢购</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const btnStyle = {
  background: '#edc6e7',
  borderColor: '#edc6e7',
  color: '#fff',
  fontWeight: 'bold',
  letterSpacing: '2px'
}

const promotions = ref([
  {
    id: 1,
    title: '618大促',
    description: '全场商品低至5折',
    tag: '限时特惠',
    tagType: 'discount',
    background: '/Images/promo-618.jpg',
    link: '/activity/618'
  },
  {
    id: 2,
    title: '数码专场',
    description: '手机数码满3000减300',
    tag: '满减',
    tagType: 'coupon',
    background: '/Images/promo-digital.jpg',
    link: '/activity/digital'
  },
  {
    id: 3,
    title: '家电焕新季',
    description: '家用电器以旧换新至高补贴800元',
    tag: '以旧换新',
    tagType: 'exchange',
    background: '/Images/promo-appliance.jpg',
    link: '/activity/appliance'
  }
])

const handlePromotionClick = (promotion) => {
  router.push(promotion.link)
}
</script>

<style scoped>
.promotion-section {
  background: #fff;
  border-radius: 32px;
  padding: 32px;
  box-shadow: 0 4px 32px rgba(0, 0, 0, 0.08);
}

.section-header {
  margin-bottom: 32px;
}

.promotions-flex {
  display: flex;
  gap: 32px;
}

.promotion-card {
  border-radius: 24px;
  background: #fff;
  box-shadow: 0 2px 16px rgba(0,0,0,0.06);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  justify-content: flex-start;
}
.promotion-card:hover {
  transform: scale(1.025) translateY(-4px);
  box-shadow: 0 8px 32px rgba(237,198,231,0.18);
}
.promotion-card.big {
  flex: 2;
  min-height: 340px;
}
.promotion-card.small {
  flex: 1;
  min-height: 160px;
  margin-bottom: 32px;
}
.promotion-side {
  display: flex;
  flex-direction: column;
  gap: 32px;
  flex: 1;
}
.promo-img {
  width: 100%;
  height: 60%;
  min-height: 120px;
  background-size: cover;
  background-position: center;
}
.promotion-content {
  width: 100%;
  padding: 28px 28px 18px 28px;
  background: linear-gradient(to bottom, rgba(255,255,255,0.0) 0%, #fff 90%);
  border-radius: 0 0 24px 24px;
  margin-top: -32px;
  box-shadow: 0 -8px 32px rgba(255,255,255,0.18);
  position: relative;
  z-index: 2;
  color: #333;
}
.promotion-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 14px;
  margin-bottom: 12px;
  background: #edc6e7;
  color: #fff;
}
.promotion-content h3 {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 8px;
}
.promotion-content p {
  font-size: 16px;
  margin-bottom: 18px;
}
</style>