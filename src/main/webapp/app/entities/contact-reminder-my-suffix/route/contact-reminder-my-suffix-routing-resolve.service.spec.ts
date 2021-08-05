jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactReminderMySuffix, ContactReminderMySuffix } from '../contact-reminder-my-suffix.model';
import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';

import { ContactReminderMySuffixRoutingResolveService } from './contact-reminder-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactReminderMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactReminderMySuffixRoutingResolveService;
    let service: ContactReminderMySuffixService;
    let resultContactReminderMySuffix: IContactReminderMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactReminderMySuffixRoutingResolveService);
      service = TestBed.inject(ContactReminderMySuffixService);
      resultContactReminderMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IContactReminderMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminderMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactReminderMySuffix).toEqual({ id: 123 });
      });

      it('should return new IContactReminderMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminderMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactReminderMySuffix).toEqual(new ContactReminderMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactReminderMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminderMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactReminderMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
