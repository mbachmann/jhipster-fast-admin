jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermissionFaService } from '../service/permission-fa.service';

import { PermissionFaComponent } from './permission-fa.component';

describe('Component Tests', () => {
  describe('PermissionFa Management Component', () => {
    let comp: PermissionFaComponent;
    let fixture: ComponentFixture<PermissionFaComponent>;
    let service: PermissionFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PermissionFaComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(PermissionFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PermissionFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PermissionFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.permissions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
