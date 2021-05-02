import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from "@angular/common/http";
import { Observable, of, throwError } from "rxjs";
import { ErrorService } from "../shared/services/error.service";
import { catchError, tap } from "rxjs/operators";

export class ServerErrorsInterceptor implements HttpInterceptor {
  constructor(
    private error: ErrorService,
  ) {
  }
  // intercept function to handle server errors
  public intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const clonedRequest = req.clone({
      withCredentials: true, // depending on needs
    });
    return next.handle(req).pipe(
      catchError(err => {
        if (err instanceof HttpErrorResponse) {
          //handle blob errors
          if (err.error instanceof Blob) {
            this.error.parseBlobError(err);
          } else {
            this.error.handleError(err);
          }
        }
        //skip handling on services;
        return of(err);
        //return throwError(err);
      }),
      tap(res => {
        if (res instanceof HttpResponse) {
        }
      })
    );
  }
}
