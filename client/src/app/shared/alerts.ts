import Swal from 'sweetalert2';
import { ToastType } from './types';

type Props = {
  toastType: ToastType,
  message: string,
  timer?: number
}

export const showAlert = async (props: Props) => {
  const toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: props.timer || 3000,
    customClass: 'sweet-alerts',
  });
  toast.fire({
    icon: props.toastType,
    title: props.message,
    customClass: 'sweet-alerts',
  });
}