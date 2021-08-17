import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBankAccountFa, BankAccountFa } from '../bank-account-fa.model';
import { BankAccountFaService } from '../service/bank-account-fa.service';

@Injectable({ providedIn: 'root' })
export class BankAccountFaRoutingResolveService implements Resolve<IBankAccountFa> {
  constructor(protected service: BankAccountFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBankAccountFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bankAccount: HttpResponse<BankAccountFa>) => {
          if (bankAccount.body) {
            return of(bankAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BankAccountFa());
  }
}
