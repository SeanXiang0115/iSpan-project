<script setup>
import { ref } from 'vue';

// Import images
import foodImage from '@/pictures/About/food.jpg';
import esgImage from '@/pictures/About/ESG.png';
import kidImage from '@/pictures/About/kid.jpg';
</script>

<template>
  <div class="about-view-gdg">
    <!-- Hero Section -->
    <section class="hero-section-gdg">
      <div class="hero-overlay-gdg">
        <div class="hero-content-gdg">
          <h1 class="hero-title-gdg">關於寶島美食導覽</h1>
          <div class="hero-divider-gdg"></div>
          <p class="hero-subtitle-gdg">探索台灣味蕾的旅程</p>
        </div>
      </div>
    </section>

    <!-- Philosophy Section -->
    <section class="philosophy-section-gdg">
      <div class="container">
        <div class="section-header-gdg">
          <h2 class="section-title-gdg">【島嶼時光，舌尖上的帶路人】</h2>
          <div class="section-underline-gdg"></div>
        </div>
        <div class="philosophy-content-gdg">
          <p class="philosophy-text-gdg">
            台灣，是一座被山海與美味餵養的寶島。
我們相信，每一道料理背後，都藏著一段島嶼的故事與職人的堅持。
<br>
<span class="highlight-gdg">「饗島」</span>以此為初衷，致力於建構全方位的食旅生態圈。我們不僅是您的味覺地圖，帶領您穿梭巷弄尋覓寶島秘境；更是您的專屬引路人，為您預約每一場期待已久的相遇；最後，我們將這份感動轉化為指尖上的美好，讓在地嚴選的滋味突破地理限制，隨時傳遞至您的餐桌。

從探索、入座到珍藏，饗島邀您踏上一場全方位的寶島美食盛宴，細品這座島嶼最真誠的款待。
          </p>
        </div>
      </div>
    </section>

    <!-- Selection Criteria Section -->
    <section class="criteria-section-gdg">
      <div class="container">
        <div class="section-header-gdg">
          <h2 class="section-title-gdg">嚴選標準</h2>
          <div class="section-underline-gdg"></div>
          <p class="section-description-gdg">我們用心篩選每一家餐廳，只為給您最值得品味的美食體驗</p>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section-gdg">
      <div class="container">
        <div class="features-grid-gdg">
          
          <!-- ESG Feature -->
          <div class="feature-card-gdg">
            <div class="feature-image-wrapper-gdg">
              <img :src="esgImage" alt="ESG環境永續" class="feature-image-gdg">
              <div class="feature-overlay-gdg"></div>
            </div>
            <div class="feature-content-gdg">
              <h3 class="feature-title-gdg">ESG 環境永續</h3>
              <p class="feature-description-gdg">
                優先推薦實踐環境保護、社會責任與公司治理的餐廳。從食材採購到營運管理，每個環節都堅持永續理念，為地球盡一份心力。
              </p>
              <div class="feature-tags-gdg">
                <span class="feature-tag-gdg">環保包裝</span>
                <span class="feature-tag-gdg">節能減碳</span>
                <span class="feature-tag-gdg">零剩食</span>
              </div>
            </div>
          </div>

          <!-- Local Farmers Feature -->
          <div class="feature-card-gdg feature-card-reverse-gdg">
            <div class="feature-image-wrapper-gdg">
              <img :src="foodImage" alt="在地小農" class="feature-image-gdg">
              <div class="feature-overlay-gdg"></div>
            </div>
            <div class="feature-content-gdg">
              <h3 class="feature-title-gdg">在地小農</h3>
              <p class="feature-description-gdg">
                支持台灣在地小農，嚴選使用當季新鮮食材的餐廳。從產地到餐桌，縮短食物里程，品嚐最純粹的台灣味道。
              </p>
              <div class="feature-tags-gdg">
                <span class="feature-tag-gdg">在地食材</span>
                <span class="feature-tag-gdg">當季鮮採</span>
                <span class="feature-tag-gdg">產地直送</span>
              </div>
            </div>
          </div>

          <!-- Family Friendly Feature -->
          <div class="feature-card-gdg">
            <div class="feature-image-wrapper-gdg">
              <img :src="kidImage" alt="親子友善" class="feature-image-gdg">
              <div class="feature-overlay-gdg"></div>
            </div>
            <div class="feature-content-gdg">
              <h3 class="feature-title-gdg">親子友善</h3>
              <p class="feature-description-gdg">
                精選提供親子友善環境的餐廳，讓家庭聚餐更加輕鬆愉快。從兒童座椅到親子廁所，完善的設施讓父母安心，孩子開心。
              </p>
              <div class="feature-tags-gdg">
                <span class="feature-tag-gdg">兒童餐具</span>
                <span class="feature-tag-gdg">遊戲空間</span>
                <span class="feature-tag-gdg">哺乳室</span>
              </div>
            </div>
          </div>

        </div>
      </div>
    </section>

    <!-- Closing Section -->
    <section class="closing-section-gdg">
      <div class="container">
        <div class="closing-content-gdg">
          <p class="closing-text-gdg">
            讓我們一起探索台灣，<br>
            品味每一處角落的美好滋味
          </p>
          <div class="closing-signature-gdg">— 寶島美食導覽團隊</div>
        </div>
      </div>
    </section>

  </div>
</template>

<style scoped lang="scss">
@import '@/assets/styles/custom.scss';

.about-view-gdg {
  min-height: 100vh;
  background-color: #fff;
}

// Hero Section
.hero-section-gdg {
  position: relative;
  height: 60vh;
  min-height: 400px;
  background: linear-gradient(135deg, rgba(159, 149, 114, 0.95) 0%, rgba(119, 111, 84, 0.95) 100%),
              url('@/pictures/About/food.jpg') center/cover;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: repeating-linear-gradient(
      0deg,
      rgba(255, 255, 255, 0.03) 0px,
      transparent 1px,
      transparent 2px,
      rgba(255, 255, 255, 0.03) 3px
    );
    pointer-events: none;
  }
}

.hero-overlay-gdg {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 2rem;
}

.hero-content-gdg {
  animation: fadeInUp 1s ease-out;
}

.hero-title-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 4rem;
  font-weight: 700;
  color: #fff;
  letter-spacing: 0.2em;
  margin-bottom: 1.5rem;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.3);

  @media (max-width: 768px) {
    font-size: 2.5rem;
  }
}

.hero-divider-gdg {
  width: 120px;
  height: 3px;
  background-color: #fff;
  margin: 0 auto 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.hero-subtitle-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 1.5rem;
  font-weight: 400;
  color: rgba(255, 255, 255, 0.95);
  letter-spacing: 0.25em;

  @media (max-width: 768px) {
    font-size: 1.2rem;
  }
}

// Philosophy Section
.philosophy-section-gdg {
  padding: 6rem 2rem;
  background-color: #fafaf8;
}

.section-header-gdg {
  text-align: center;
  margin-bottom: 3rem;
}

.section-title-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 2.5rem;
  font-weight: 700;
  color: $gdg-gold;
  letter-spacing: 0.15em;
  margin-bottom: 1rem;
}

.section-underline-gdg {
  width: 80px;
  height: 3px;
  background-color: $gdg-gold;
  margin: 0 auto;
}

.section-description-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 1.1rem;
  color: $gdg-gold-dark;
  margin-top: 1.5rem;
  letter-spacing: 0.05em;
}

.philosophy-content-gdg {
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
}

.philosophy-text-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 1.5rem;
  line-height: 2.2;
  color: #333;
  letter-spacing: 0.08em;
  
  @media (max-width: 768px) {
    font-size: 1.2rem;
    br {
      display: none;
    }
  }
}

.highlight-gdg {
  color: $gdg-gold;
  font-weight: 600;
  position: relative;
  
  &::after {
    content: '';
    position: absolute;
    bottom: -2px;
    left: 0;
    width: 100%;
    height: 2px;
    background-color: $gdg-gold;
    opacity: 0.3;
  }
}

// Criteria Section
.criteria-section-gdg {
  padding: 4rem 2rem 2rem;
  background-color: #fff;
}

// Features Section
.features-section-gdg {
  padding: 2rem 2rem 6rem;
  background-color: #fff;
}

.features-grid-gdg {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 4rem;
}

.feature-card-gdg {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 3rem;
  align-items: center;
  transition: transform 0.3s ease;

  @media (max-width: 968px) {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  &:hover {
    transform: translateY(-5px);
  }
}

.feature-card-reverse-gdg {
  .feature-image-wrapper-gdg {
    order: 2;
  }

  .feature-content-gdg {
    order: 1;
  }

  @media (max-width: 968px) {
    .feature-image-wrapper-gdg {
      order: 1;
    }

    .feature-content-gdg {
      order: 2;
    }
  }
}

.feature-image-wrapper-gdg {
  position: relative;
  overflow: hidden;
  border-radius: 0;
  aspect-ratio: 4/3;
  border: 3px solid $gdg-gold;
  box-shadow: 12px 12px 0px rgba(160, 150, 115, 0.2);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 16px 16px 0px rgba(160, 150, 115, 0.3);
    transform: translate(-3px, -3px);
  }
}

.feature-image-gdg {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;

  .feature-image-wrapper-gdg:hover & {
    transform: scale(1.05);
  }
}

.feature-overlay-gdg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.3) 0%, transparent 50%);
  pointer-events: none;
}

.feature-content-gdg {
  padding: 1rem;
}

.feature-title-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 2rem;
  font-weight: 700;
  color: $gdg-gold;
  letter-spacing: 0.12em;
  margin-bottom: 1.5rem;
  position: relative;
  display: inline-block;

  &::after {
    content: '';
    position: absolute;
    bottom: -8px;
    left: 0;
    width: 60px;
    height: 3px;
    background-color: $gdg-gold;
  }
}

.feature-description-gdg {
  font-family: 'Noto Sans TC', sans-serif;
  font-size: 1.1rem;
  line-height: 1.9;
  color: #555;
  margin-bottom: 2rem;
  letter-spacing: 0.05em;
}

.feature-tags-gdg {
  display: flex;
  gap: 0.8rem;
  flex-wrap: wrap;
}

.feature-tag-gdg {
  display: inline-block;
  padding: 0.5rem 1.2rem;
  background-color: $gdg-bg-light;
  color: $gdg-gold-dark;
  font-size: 0.9rem;
  font-weight: 500;
  letter-spacing: 0.05em;
  border: 1px solid $gdg-gold;
  transition: all 0.3s ease;

  &:hover {
    background-color: $gdg-gold;
    color: #fff;
    transform: translateY(-2px);
  }
}

// Closing Section
.closing-section-gdg {
  padding: 5rem 2rem;
  background: linear-gradient(135deg, rgba(159, 149, 114, 0.08) 0%, rgba(119, 111, 84, 0.08) 100%);
  text-align: center;
}

.closing-content-gdg {
  max-width: 700px;
  margin: 0 auto;
}

.closing-text-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 1.8rem;
  line-height: 2;
  color: $gdg-gold-dark;
  letter-spacing: 0.1em;
  margin-bottom: 2rem;

  @media (max-width: 768px) {
    font-size: 1.4rem;
    br {
      display: none;
    }
  }
}

.closing-signature-gdg {
  font-family: 'Noto Serif TC', serif;
  font-size: 1.2rem;
  color: $gdg-gold;
  letter-spacing: 0.15em;
  font-style: italic;
}

// Animations
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>