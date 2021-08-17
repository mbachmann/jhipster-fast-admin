import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ReportingEntityType } from 'app/entities/enumerations/reporting-entity-type.model';
import { IEffortFa, EffortFa } from '../effort-fa.model';

import { EffortFaService } from './effort-fa.service';

describe('Service Tests', () => {
  describe('EffortFa Service', () => {
    let service: EffortFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEffortFa;
    let expectedResult: IEffortFa | IEffortFa[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EffortFaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        remoteId: 0,
        userId: 0,
        userName: 'AAAAAAA',
        entityType: ReportingEntityType.PROJECT,
        entityId: 0,
        duration: 0,
        date: currentDate,
        activityName: 'AAAAAAA',
        notes: 'AAAAAAA',
        isInvoiced: false,
        updated: currentDate,
        hourlyRate: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
            updated: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EffortFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
            updated: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            updated: currentDate,
          },
          returnedFromService
        );

        service.create(new EffortFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EffortFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            userId: 1,
            userName: 'BBBBBB',
            entityType: 'BBBBBB',
            entityId: 1,
            duration: 1,
            date: currentDate.format(DATE_FORMAT),
            activityName: 'BBBBBB',
            notes: 'BBBBBB',
            isInvoiced: true,
            updated: currentDate.format(DATE_TIME_FORMAT),
            hourlyRate: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            updated: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EffortFa', () => {
        const patchObject = Object.assign(
          {
            userName: 'BBBBBB',
            entityType: 'BBBBBB',
            duration: 1,
            date: currentDate.format(DATE_FORMAT),
            activityName: 'BBBBBB',
            isInvoiced: true,
          },
          new EffortFa()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
            updated: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EffortFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
            userId: 1,
            userName: 'BBBBBB',
            entityType: 'BBBBBB',
            entityId: 1,
            duration: 1,
            date: currentDate.format(DATE_FORMAT),
            activityName: 'BBBBBB',
            notes: 'BBBBBB',
            isInvoiced: true,
            updated: currentDate.format(DATE_TIME_FORMAT),
            hourlyRate: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
            updated: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EffortFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEffortFaToCollectionIfMissing', () => {
        it('should add a EffortFa to an empty array', () => {
          const effort: IEffortFa = { id: 123 };
          expectedResult = service.addEffortFaToCollectionIfMissing([], effort);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(effort);
        });

        it('should not add a EffortFa to an array that contains it', () => {
          const effort: IEffortFa = { id: 123 };
          const effortCollection: IEffortFa[] = [
            {
              ...effort,
            },
            { id: 456 },
          ];
          expectedResult = service.addEffortFaToCollectionIfMissing(effortCollection, effort);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EffortFa to an array that doesn't contain it", () => {
          const effort: IEffortFa = { id: 123 };
          const effortCollection: IEffortFa[] = [{ id: 456 }];
          expectedResult = service.addEffortFaToCollectionIfMissing(effortCollection, effort);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(effort);
        });

        it('should add only unique EffortFa to an array', () => {
          const effortArray: IEffortFa[] = [{ id: 123 }, { id: 456 }, { id: 62191 }];
          const effortCollection: IEffortFa[] = [{ id: 123 }];
          expectedResult = service.addEffortFaToCollectionIfMissing(effortCollection, ...effortArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const effort: IEffortFa = { id: 123 };
          const effort2: IEffortFa = { id: 456 };
          expectedResult = service.addEffortFaToCollectionIfMissing([], effort, effort2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(effort);
          expect(expectedResult).toContain(effort2);
        });

        it('should accept null and undefined values', () => {
          const effort: IEffortFa = { id: 123 };
          expectedResult = service.addEffortFaToCollectionIfMissing([], null, effort, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(effort);
        });

        it('should return initial array if no EffortFa is added', () => {
          const effortCollection: IEffortFa[] = [{ id: 123 }];
          expectedResult = service.addEffortFaToCollectionIfMissing(effortCollection, undefined, null);
          expect(expectedResult).toEqual(effortCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
