import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILayout, Layout } from '../layout.model';

import { LayoutService } from './layout.service';

describe('Service Tests', () => {
  describe('Layout Service', () => {
    let service: LayoutService;
    let httpMock: HttpTestingController;
    let elemDefault: ILayout;
    let expectedResult: ILayout | ILayout[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LayoutService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        remoteId: 0,
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

      it('should create a Layout', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Layout()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Layout', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Layout', () => {
        const patchObject = Object.assign(
          {
            remoteId: 1,
          },
          new Layout()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Layout', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            remoteId: 1,
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

      it('should delete a Layout', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLayoutToCollectionIfMissing', () => {
        it('should add a Layout to an empty array', () => {
          const layout: ILayout = { id: 123 };
          expectedResult = service.addLayoutToCollectionIfMissing([], layout);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(layout);
        });

        it('should not add a Layout to an array that contains it', () => {
          const layout: ILayout = { id: 123 };
          const layoutCollection: ILayout[] = [
            {
              ...layout,
            },
            { id: 456 },
          ];
          expectedResult = service.addLayoutToCollectionIfMissing(layoutCollection, layout);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Layout to an array that doesn't contain it", () => {
          const layout: ILayout = { id: 123 };
          const layoutCollection: ILayout[] = [{ id: 456 }];
          expectedResult = service.addLayoutToCollectionIfMissing(layoutCollection, layout);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(layout);
        });

        it('should add only unique Layout to an array', () => {
          const layoutArray: ILayout[] = [{ id: 123 }, { id: 456 }, { id: 28667 }];
          const layoutCollection: ILayout[] = [{ id: 123 }];
          expectedResult = service.addLayoutToCollectionIfMissing(layoutCollection, ...layoutArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const layout: ILayout = { id: 123 };
          const layout2: ILayout = { id: 456 };
          expectedResult = service.addLayoutToCollectionIfMissing([], layout, layout2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(layout);
          expect(expectedResult).toContain(layout2);
        });

        it('should accept null and undefined values', () => {
          const layout: ILayout = { id: 123 };
          expectedResult = service.addLayoutToCollectionIfMissing([], null, layout, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(layout);
        });

        it('should return initial array if no Layout is added', () => {
          const layoutCollection: ILayout[] = [{ id: 123 }];
          expectedResult = service.addLayoutToCollectionIfMissing(layoutCollection, undefined, null);
          expect(expectedResult).toEqual(layoutCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
