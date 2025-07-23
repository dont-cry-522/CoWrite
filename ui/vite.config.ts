import { defineConfig } from 'vite'
import path from 'node:path'
import vue from '@vitejs/plugin-vue'
import electron from 'vite-plugin-electron/simple'
import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'
import Components from 'unplugin-vue-components/vite'

export default defineConfig({
  plugins: [
    vue(),
    Components({
      resolvers: [
        IconsResolver({
          componentPrefix: 'Icon',
        }),
      ],
    }),
    Icons({
      autoInstall: true,
    }),
    electron({
      main: {
        entry: 'electron/main.ts',
      },
      preload: {
        input: path.join(__dirname, 'electron/preload.ts'),
      },
      renderer: process.env.NODE_ENV === 'test' ? undefined : {},
    }),
  ],
})
