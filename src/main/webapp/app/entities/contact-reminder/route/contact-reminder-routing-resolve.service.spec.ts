jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactReminder, ContactReminder } from '../contact-reminder.model';
import { ContactReminderService } from '../service/contact-reminder.service';

import { ContactReminderRoutingResolveService } from './contact-reminder-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactReminder routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactReminderRoutingResolveService;
    let service: ContactReminderService;
    let resultContactReminder: IContactReminder | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactReminderRoutingResolveService);
      service = TestBed.inject(ContactReminderService);
      resultContactReminder = undefined;
    });

    describe('resolve', () => {
      it('should return IContactReminder returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminder = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactReminder).toEqual({ id: 123 });
      });

      it('should return new IContactReminder if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminder = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactReminder).toEqual(new ContactReminder());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactReminder })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactReminder = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactReminder).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
