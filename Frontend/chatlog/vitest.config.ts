import Vue from '@vitejs/plugin-vue';
import Components from 'unplugin-vue-components/vite';
import AutoImport from 'unplugin-auto-import/vite'
import { defineConfig } from 'vitest/config';

export default defineConfig({
  plugins: [
    Vue(),
    AutoImport({
      imports: ['vue', 'vue-router'],
      dirs: ['./composables'],
      vueTemplate: true
    }),
    Components({
      dirs: [
        './components/'
      ],
      dts: true,
      directoryAsNamespace: true
    })
  ],
  test: {
    globals: true,
    environment: 'jsdom',
  }
});