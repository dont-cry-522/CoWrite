import { App, createVNode, render } from 'vue'
import NotifyContainer from '../components/NotifyContainer.vue'

let vm: any

export default {
    install(app: App) {
        const container = document.createElement('div')
        document.body.appendChild(container)

        const vnode = createVNode(NotifyContainer)
        render(vnode, container)

        vm = vnode.component?.exposed

        app.config.globalProperties.$notify = {
            success: (title: string, desc?: string, duration = 3000) => vm?.add(title, 'success', duration, desc),
            error: (title: string, desc?: string, duration = 3000) => vm?.add(title, 'error', duration, desc),
            info: (title: string, desc?: string, duration = 3000) => vm?.add(title, 'info', duration, desc),
            warning: (title: string, desc?: string, duration = 3000) => vm?.add(title, 'warning', duration, desc),
        }
    }
}
