import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReportingActivity, ReportingActivity } from '../reporting-activity.model';

import { ReportingActivityService } from './reporting-activity.service';

describe('Service Tests', () => {
  describe('ReportingActivity Service', () => {
    let service: ReportingActivityService;
    let httpMock: HttpTestingController;
    let elemDefault: IReportingActivity;
    let expectedResult: IReportingActivity | IReportingActivity[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReportingActivityService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
        name: 'AAAAAAA',
        useServicePrice: false,
        inactiv: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ReportingActivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ReportingActivity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ReportingActivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            useServicePrice: true,
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ReportingActivity', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
          },
          new ReportingActivity()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ReportingActivity', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            name: 'BBBBBB',
            useServicePrice: true,
            inactiv: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ReportingActivity', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReportingActivityToCollectionIfMissing', () => {
        it('should add a ReportingActivity to an empty array', () => {
          const reportingActivity: IReportingActivity = { id: 123 };
          expectedResult = service.addReportingActivityToCollectionIfMissing([], reportingActivity);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reportingActivity);
        });

        it('should not add a ReportingActivity to an array that contains it', () => {
          const reportingActivity: IReportingActivity = { id: 123 };
          const reportingActivityCollection: IReportingActivity[] = [
            {
              ...reportingActivity,
            },
            { id: 456 },
          ];
          expectedResult = service.addReportingActivityToCollectionIfMissing(reportingActivityCollection, reportingActivity);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ReportingActivity to an array that doesn't contain it", () => {
          const reportingActivity: IReportingActivity = { id: 123 };
          const reportingActivityCollection: IReportingActivity[] = [{ id: 456 }];
          expectedResult = service.addReportingActivityToCollectionIfMissing(reportingActivityCollection, reportingActivity);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reportingActivity);
        });

        it('should add only unique ReportingActivity to an array', () => {
          const reportingActivityArray: IReportingActivity[] = [{ id: 123 }, { id: 456 }, { id: 17730 }];
          const reportingActivityCollection: IReportingActivity[] = [{ id: 123 }];
          expectedResult = service.addReportingActivityToCollectionIfMissing(reportingActivityCollection, ...reportingActivityArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const reportingActivity: IReportingActivity = { id: 123 };
          const reportingActivity2: IReportingActivity = { id: 456 };
          expectedResult = service.addReportingActivityToCollectionIfMissing([], reportingActivity, reportingActivity2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(reportingActivity);
          expect(expectedResult).toContain(reportingActivity2);
        });

        it('should accept null and undefined values', () => {
          const reportingActivity: IReportingActivity = { id: 123 };
          expectedResult = service.addReportingActivityToCollectionIfMissing([], null, reportingActivity, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(reportingActivity);
        });

        it('should return initial array if no ReportingActivity is added', () => {
          const reportingActivityCollection: IReportingActivity[] = [{ id: 123 }];
          expectedResult = service.addReportingActivityToCollectionIfMissing(reportingActivityCollection, undefined, null);
          expect(expectedResult).toEqual(reportingActivityCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
