<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const goTo = (path) => {
  router.push(path);
};
</script>

<template>
  <footer class="footer-gdg mt-auto py-5">
    <div class="container">
      <div class="row">
        <!-- Social / Contact (Left 1/3) -->
        <div class="col-md-4 mb-4 mb-md-0 d-flex flex-column align-items-start">
          <h5 class="text-uppercase mb-3 fw-bold text-gdg">聯絡我們</h5>
          <p class="small text-muted mb-4">
            我們重視您的意見，歡迎隨時與我們聯繫。
          </p>
          <a class="btn btn-gdg d-flex align-items-center gap-2" href="#" @click.prevent="goTo('/feedback')">
            <i class="bi bi-chat-dots-fill"></i>
            <span>意見回饋</span>
          </a>
        </div>

        <!-- Links (Right 2/3) -->
        <div class="col-md-8">
          <div class="row">
            <!-- Main Navigation -->
            <div class="col-6 col-md-4 mb-3">
              <h6 class="text-uppercase mb-3 fw-bold text-gdg">網站導覽</h6>
              <ul class="list-unstyled">
                <li class="mb-2"><a href="#" @click.prevent="goTo('/')" class="footer-link">首頁</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/about')" class="footer-link">關於我們</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/mapSearch')" class="footer-link">美味座標</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/shopStore')" class="footer-link">質感選物</a></li>
              </ul>
            </div>

            <!-- Shop / Member
            <div class="col-6 col-md-4 mb-3">
              <h6 class="text-uppercase mb-3 fw-bold text-gdg">會員服務</h6>
              <ul class="list-unstyled">
                <li class="mb-2"><a href="#" @click.prevent="goTo('/cart')" class="footer-link">購物車</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/userInfo')" class="footer-link">會員中心</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/storeRegistration')" class="footer-link">申請成為商家</a></li>
              </ul>
            </div> -->

            <!-- Owner Services -->
            <!-- <div class="col-6 col-md-4 mb-3">
              <h6 class="text-uppercase mb-3 fw-bold text-gdg">商家專區</h6>
              <ul class="list-unstyled">
                <li class="mb-2"><a href="#" @click.prevent="goTo('/owner/storeInfo')" class="footer-link">店家資訊編輯</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/owner/bookings/seats')" class="footer-link">座位與時段管理</a></li>
                <li class="mb-2"><a href="#" @click.prevent="goTo('/owner/bookings/data')" class="footer-link">訂位資料管理</a></li>
              </ul>
            </div> -->
          </div>
        </div>
      </div>
      
      <div class="row mt-4 border-top border-secondary pt-3">
        <div class="col-12 text-center small text-muted">
          &copy; 2026 iSpan Project. All Rights Reserved.
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
/* Scoped styles if needed, but primarily using custom.scss */
</style>
