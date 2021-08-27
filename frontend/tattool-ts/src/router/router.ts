import { createWebHistory, createRouter, RouteComponent, RouteRecordRaw } from "vue-router";
import Recorder from "@/components/recordings/editor/Index.vue";
import Recordings from "@/components/recordings/list/Index.vue";
import RecordingFilter from "@/components/recordingfilters/editor/Index.vue";
import RecordingFilters from "@/components/recordingfilters/list/Index.vue";
import Dataset from "@/components/datasets/editor/Index.vue";
import Datasets from "@/components/datasets/list/Index.vue";
import Dashboard from "@/components/dashboard/Index.vue";
import Run from "@/components/runs/Index.vue";
import LiveRunViewer from "@/components/runs/LiveRunViewer.vue";
import useUIState from "@/state/useUIState";
const { MenuItem, setCurrentMenuId } = useUIState();

const routes = [
  {
    path: "/dashboard",
    name: "dashboard",
    alias: ["/"],
    component: Dashboard,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.DASHBOARD)
    }
  },
  {
    path: "/recordings",
    component: Recordings,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.RECORDINGS)
    }
  },
  {
    path: "/edit-recording/:id",
    component: Recorder,
    props: true,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.RECORDINGS)
    }
  },
  {
    path: "/new-recording",
    component: Recorder,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.RECORDINGS)
    }
  },
  {
    path: "/recordingfilters",
    component: RecordingFilters,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.RECORDINGFILTERS)
    }
  },
  {
    path: "/edit-recordingfilter/:id",
    component: RecordingFilter,
    props: true,
    meta: { requiresAuth: false },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.RECORDINGFILTERS)
    }
  },
  {
    path: "/new-recordingfilter",
    component: RecordingFilter,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.RECORDINGFILTERS)
    }

  },
  {
    path: "/datasets",
    component: Datasets,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.DATASETS)
    }

  },
  {
    path: "/edit-dataset/:id",
    component: Dataset,
    props: true,
    meta: { requiresAuth: false },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.DATASETS)
    }

  },
  {
    path: "/new-dataset",
    component: Dataset,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.DATASETS)
    }

  },

  {
    path: "/runs/:id",
    component: Run,
    props: true,
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.DASHBOARD)
    }

  },
  {
    path: "/live-run/:reference/:type",
    component: LiveRunViewer,
    props: (route: any) => ({ reference: route.params.reference, type: route.params.type, debug: route.query.debug }),
    meta: { requiresAuth: true },
    beforeEnter: (to: any, from: any) => {
      setCurrentMenuId(MenuItem.DASHBOARD)
    }

  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
