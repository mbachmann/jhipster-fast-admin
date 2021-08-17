import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogCategoryFaDetailComponent } from './catalog-category-fa-detail.component';

describe('Component Tests', () => {
  describe('CatalogCategoryFa Management Detail Component', () => {
    let comp: CatalogCategoryFaDetailComponent;
    let fixture: ComponentFixture<CatalogCategoryFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogCategoryFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogCategoryFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogCategoryFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load catalogCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.catalogCategory).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
