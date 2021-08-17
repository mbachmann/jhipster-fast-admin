import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogCategoryDetailComponent } from './catalog-category-detail.component';

describe('Component Tests', () => {
  describe('CatalogCategory Management Detail Component', () => {
    let comp: CatalogCategoryDetailComponent;
    let fixture: ComponentFixture<CatalogCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogCategory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogCategoryDetailComponent);
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
