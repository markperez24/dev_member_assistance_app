import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export class CustomHttpInterceptor implements HttpInterceptor {
  constructor() {
  }

  //intercept function
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    /*const httpRequest = req.clone({
      headers: new HttpHeaders({
        'Cache-Control': 'no-store, no-cache'
      })
    });*/
    if (!req.headers.has('Authorization')) {
      const httpRequest = req.clone({
        headers: new HttpHeaders({
          'Cache-Control': 'no-store, no-cache'
        })
      });
      return next.handle(httpRequest);
    } else
      return next.handle(req);
  }

  /*intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next
      .handle(req)
      .pipe(
        tap((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
          }
        }));
  }*/
}
