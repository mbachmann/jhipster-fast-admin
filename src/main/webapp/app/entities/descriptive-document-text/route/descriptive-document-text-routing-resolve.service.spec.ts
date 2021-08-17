jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDescriptiveDocumentText, DescriptiveDocumentText } from '../descriptive-document-text.model';
import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';

import { DescriptiveDocumentTextRoutingResolveService } from './descriptive-document-text-routing-resolve.service';

describe('Service Tests', () => {
  describe('DescriptiveDocumentText routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DescriptiveDocumentTextRoutingResolveService;
    let service: DescriptiveDocumentTextService;
    let resultDescriptiveDocumentText: IDescriptiveDocumentText | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DescriptiveDocumentTextRoutingResolveService);
      service = TestBed.inject(DescriptiveDocumentTextService);
      resultDescriptiveDocumentText = undefined;
    });

    describe('resolve', () => {
      it('should return IDescriptiveDocumentText returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDescriptiveDocumentText = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDescriptiveDocumentText).toEqual({ id: 123 });
      });

      it('should return new IDescriptiveDocumentText if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDescriptiveDocumentText = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDescriptiveDocumentText).toEqual(new DescriptiveDocumentText());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DescriptiveDocumentText })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDescriptiveDocumentText = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDescriptiveDocumentText).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
