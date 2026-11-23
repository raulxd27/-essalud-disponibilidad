import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const token = localStorage.getItem('essalud_token');

  if (token) {
    const solicitud = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(solicitud);
  }

  return next(req);
};
