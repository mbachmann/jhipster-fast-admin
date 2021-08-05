jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactReminderFa, ContactReminderFa } from '../contact-reminder-fa.model';
import { ContactReminderFaService } from '../service/contact-reminder-fa.service';

import { ContactReminderFaRoutingResolveService } from './contact-reminder-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactReminderFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactReminderFaRoutingResolveService;
    let service: ContactReminderFaService;
    let resultContactReminderFa: IContactReminderFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactReminderFaRoutingResolveService);
      service = TestBed.inject(ContactReminderFaService);
      resultContactReminderFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactReminderFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminderFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactReminderFa).toEqual({ id: 123 });
      });

      it('should return new IContactReminderFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminderFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactReminderFa).toEqual(new ContactReminderFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactReminderFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminderFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactReminderFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
