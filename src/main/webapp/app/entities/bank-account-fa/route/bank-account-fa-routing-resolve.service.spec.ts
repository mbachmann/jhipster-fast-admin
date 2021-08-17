jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBankAccountFa, BankAccountFa } from '../bank-account-fa.model';
import { BankAccountFaService } from '../service/bank-account-fa.service';

import { BankAccountFaRoutingResolveService } from './bank-account-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('BankAccountFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BankAccountFaRoutingResolveService;
    let service: BankAccountFaService;
    let resultBankAccountFa: IBankAccountFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BankAccountFaRoutingResolveService);
      service = TestBed.inject(BankAccountFaService);
      resultBankAccountFa = undefined;
    });

    describe('resolve', () => {
      it('should return IBankAccountFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBankAccountFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBankAccountFa).toEqual({ id: 123 });
      });

      it('should return new IBankAccountFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBankAccountFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBankAccountFa).toEqual(new BankAccountFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BankAccountFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBankAccountFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBankAccountFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
