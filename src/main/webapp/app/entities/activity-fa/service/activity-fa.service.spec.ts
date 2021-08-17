import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IActivityFa, ActivityFa } from '../activity-fa.model';

import { ActivityFaService } from './activity-fa.service';

describe('Service Tests', () => {
  describe('ActivityFa Service', () => {
    let service: ActivityFaService;
    let httpMock: HttpTestingController;
    let elemDefault: IActivityFa;
    let expectedResult: IActivityFa | IActivityFa[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ActivityFaService);
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

      it('should create a ActivityFa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ActivityFa()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ActivityFa', () => {
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

      it('should partial update a ActivityFa', () => {
        const patchObject = Object.assign({}, new ActivityFa());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ActivityFa', () => {
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

      it('should delete a ActivityFa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addActivityFaToCollectionIfMissing', () => {
        it('should add a ActivityFa to an empty array', () => {
          const activity: IActivityFa = { id: 123 };
          expectedResult = service.addActivityFaToCollectionIfMissing([], activity);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(activity);
        });

        it('should not add a ActivityFa to an array that contains it', () => {
          const activity: IActivityFa = { id: 123 };
          const activityCollection: IActivityFa[] = [
            {
              ...activity,
            },
            { id: 456 },
          ];
          expectedResult = service.addActivityFaToCollectionIfMissing(activityCollection, activity);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ActivityFa to an array that doesn't contain it", () => {
          const activity: IActivityFa = { id: 123 };
          const activityCollection: IActivityFa[] = [{ id: 456 }];
          expectedResult = service.addActivityFaToCollectionIfMissing(activityCollection, activity);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(activity);
        });

        it('should add only unique ActivityFa to an array', () => {
          const activityArray: IActivityFa[] = [{ id: 123 }, { id: 456 }, { id: 22429 }];
          const activityCollection: IActivityFa[] = [{ id: 123 }];
          expectedResult = service.addActivityFaToCollectionIfMissing(activityCollection, ...activityArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const activity: IActivityFa = { id: 123 };
          const activity2: IActivityFa = { id: 456 };
          expectedResult = service.addActivityFaToCollectionIfMissing([], activity, activity2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(activity);
          expect(expectedResult).toContain(activity2);
        });

        it('should accept null and undefined values', () => {
          const activity: IActivityFa = { id: 123 };
          expectedResult = service.addActivityFaToCollectionIfMissing([], null, activity, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(activity);
        });

        it('should return initial array if no ActivityFa is added', () => {
          const activityCollection: IActivityFa[] = [{ id: 123 }];
          expectedResult = service.addActivityFaToCollectionIfMissing(activityCollection, undefined, null);
          expect(expectedResult).toEqual(activityCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
